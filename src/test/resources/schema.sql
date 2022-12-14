DROP TABLE IF EXISTS device_state;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS command;
DROP TABLE IF EXISTS device;
DROP TABLE IF EXISTS unit;
DROP TABLE IF EXISTS app_user;

DROP TYPE IF EXISTS device_type;
DROP TYPE IF EXISTS command_status;
DROP TYPE IF EXISTS command_type;
DROP TYPE IF EXISTS message_type;

DROP FUNCTION IF EXISTS tr_insert_device_state;
DROP FUNCTION IF EXISTS tr_update_device_state;

CREATE TABLE app_user
(
    id      SERIAL      NOT NULL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    deleted BOOLEAN     NOT NULL DEFAULT false
);

CREATE TABLE unit
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    user_id INTEGER     NOT NULL REFERENCES app_user ON DELETE CASCADE,
    deleted BOOLEAN     NOT NULL DEFAULT false
);

CREATE TYPE device_type AS ENUM ('TRACKER', 'BEACON');

CREATE TABLE device
(
    id           SERIAL PRIMARY KEY,
    imei         VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    type         device_type NOT NULL,
    unit_id      INTEGER     NOT NULL REFERENCES unit ON DELETE CASCADE,
    deleted      BOOLEAN     NOT NULL DEFAULT false
);

CREATE TYPE command_status AS ENUM ('NEW', 'SENT', 'SUCCESS', 'ERROR', 'TIMEOUT');
CREATE TYPE command_type AS ENUM ('COMMAND', 'ANSWER');

CREATE TABLE command
(
    id        BIGSERIAL PRIMARY KEY,
    text      TEXT           NOT NULL,
    status    command_status NOT NULL,
    device_id INTEGER        NOT NULL REFERENCES device ON DELETE CASCADE,
    type      command_type   NOT NULL
);

CREATE TYPE message_type AS ENUM ('VALID', 'WRONG_ORDER', 'INCORRECT');

CREATE TABLE message
(
    id               BIGSERIAL PRIMARY KEY,
    device_id        INTEGER      NOT NULL REFERENCES device ON DELETE CASCADE,
    time             TIMESTAMP(0) NOT NULL,
    latitude         FLOAT(4)     NOT NULL,
    longitude        FLOAT(4)     NOT NULL,
    speed            SMALLINT     NOT NULL,
    course           SMALLINT     NOT NULL,
    altitude         SMALLINT     NOT NULL,
    amount_satellite SMALLINT     NOT NULL,
    gsm_level        SMALLINT     NOT NULL,
    onboard_voltage  SMALLINT     NOT NULL,
    eco_cornering    SMALLINT     NOT NULL,
    eco_acceleration SMALLINT     NOT NULL,
    eco_braking      SMALLINT     NOT NULL,
    type             message_type NOT NULL,
    gps_odometer     INTEGER      NOT NULL,
    ignition         SMALLINT     NOT NULL,
    engine_time      INTEGER      NOT NULL,
    shock            SMALLINT     NOT NULL,
    created_time     TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted          BOOLEAN      NOT NULL DEFAULT false
);

CREATE TABLE device_state
(
    device_id       INTEGER PRIMARY KEY REFERENCES device ON DELETE CASCADE,
    last_message_id BIGINT REFERENCES message UNIQUE
);

--- UNIQUE INDEXES

CREATE UNIQUE INDEX unique_imei_not_deleted_device
    ON device (imei) WHERE (deleted = false);

CREATE UNIQUE INDEX unique_name_not_deleted_users
    ON app_user (name) WHERE (deleted = false);

CREATE UNIQUE INDEX unique_name_not_deleted_units
    ON unit (name) WHERE (deleted = false);

--------------

--- TRIGGERS

CREATE OR REPLACE FUNCTION tr_insert_device_state() RETURNS TRIGGER AS
'
    BEGIN

        INSERT INTO device_state(device_id)
        VALUES (NEW.id);
        RETURN NEW;

    END;
' LANGUAGE plpgsql;

CREATE TRIGGER after_insert_device
    AFTER INSERT
    ON device
    FOR EACH ROW
EXECUTE PROCEDURE tr_insert_device_state();

CREATE FUNCTION tr_update_device_state() RETURNS TRIGGER AS
'
    BEGIN

        IF NEW.type = ''VALID'' THEN

            UPDATE device_state
            SET last_message_id = NEW.id
            WHERE device_state.device_id = NEW.device_id;

        END IF;

        RETURN NEW;

    END;
' LANGUAGE plpgsql;

CREATE TRIGGER after_insert_message
    AFTER INSERT
    ON message
    FOR EACH ROW
EXECUTE PROCEDURE tr_update_device_state();

--------------






