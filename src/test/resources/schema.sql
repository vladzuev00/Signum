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

CREATE TYPE message_type AS ENUM ('VALID', 'CORRECT', 'INCORRECT');

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
    created_time     TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted          BOOLEAN      NOT NULL DEFAULT false
);

CREATE TABLE device_state
(
    device_id            INTEGER PRIMARY KEY REFERENCES device ON DELETE CASCADE,
    last_message_id      BIGINT REFERENCES message,
    last_valid_latitude  FLOAT(4),
    last_valid_longitude FLOAT(4)
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

        IF NEW.type IN (''VALID'', ''CORRECT'') THEN

            UPDATE device_state
            SET last_message_id = NEW.id
            WHERE device_state.device_id = NEW.device_id;

            IF NEW.type = ''VALID'' THEN
                UPDATE device_state
                SET last_valid_latitude  = NEW.latitude,
                    last_valid_longitude = NEW.longitude
                WHERE device_state.device_id = NEW.device_id;
            END IF;

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






