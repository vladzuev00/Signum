CREATE TABLE app_user
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    deleted BOOLEAN     NOT NULL DEFAULT false
);

CREATE UNIQUE INDEX unique_name_not_deleted_users
    ON app_user (name) WHERE (deleted = false);

CREATE TABLE tracker
(
    id           SERIAL PRIMARY KEY,
    imei         VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    deleted      BOOLEAN     NOT NULL DEFAULT false
);

CREATE UNIQUE INDEX unique_imei_not_deleted_trackers
    ON tracker (imei) WHERE (deleted = false);

CREATE TABLE unit
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    user_id    INTEGER     NOT NULL REFERENCES app_user,
    tracker_id INTEGER     NOT NULL REFERENCES tracker UNIQUE,
    deleted    BOOLEAN     NOT NULL DEFAULT false

);

CREATE UNIQUE INDEX unique_name_not_deleted_units
    ON unit (name) WHERE (deleted = false);

CREATE TABLE message
(
    id               BIGSERIAL PRIMARY KEY,
    unit_id          INTEGER      NOT NULL REFERENCES unit,
    time             TIMESTAMP(0) NOT NULL,
    latitude         FLOAT(4)     NOT NULL,
    longitude        FLOAT(4)     NOT NULL,
    speed            SMALLINT     NOT NULL,
    course           SMALLINT     NOT NULL,
    altitude         SMALLINT     NOT NULL,
    amount_satellite SMALLINT     NOT NULL,
    hdop             FLOAT(4)     NOT NULL,
    params           TEXT         NOT NULL,
    deleted          BOOLEAN      NOT NULL DEFAULT false
);

CREATE TABLE command(
                        id BIGSERIAL PRIMARY KEY,
                        text TEXT NOT NULL,
                        status VARCHAR(64) NOT NULL,
                        tracker_id INTEGER NOT NULL REFERENCES tracker,
                        type VARCHAR(64) NOT NULL
);

ALTER TABLE command
    ADD CONSTRAINT valid_command_status
        CHECK (status IN ('NEW', 'SENT', 'SUCCESS', 'ERROR', 'TIMEOUT'));

ALTER TABLE command
    ADD CONSTRAINT valid_command_type
        CHECK(type IN ('COMMAND', 'ANSWER'));

ALTER TABLE tracker
    RENAME TO device;

ALTER TABLE device
    ADD COLUMN type VARCHAR(64) NOT NULL;

ALTER TABLE device
    ADD CONSTRAINT correct_type CHECK (type IN ('TRACKER', 'BEACON'));

ALTER TABLE message
    DROP COLUMN unit_id;

ALTER TABLE message
    ADD COLUMN device_id INTEGER NOT NULL;

ALTER TABLE message
    ADD CONSTRAINT fk_message_to_device FOREIGN KEY (device_id) REFERENCES device(id);

ALTER TABLE unit
    RENAME COLUMN tracker_id TO device_id;

ALTER TABLE message
    ADD COLUMN gsm_level SMALLINT NOT NULL;

ALTER TABLE message
    ADD COLUMN onboard_voltage SMALLINT NOT NULL;

ALTER TABLE message
    ADD COLUMN eco_acelleration SMALLINT NOT NULL;

ALTER TABLE message
    ADD COLUMN eco_brake SMALLINT NOT NULL;

ALTER TABLE message
    ADD COLUMN eco_cornering SMALLINT NOT NULL;

ALTER TABLE command
    RENAME COLUMN tracker_id TO device_id;

ALTER TABLE message
    RENAME COLUMN gsm_level TO gsm_level_percent;

ALTER TABLE message
    RENAME COLUMN onboard_voltage TO voltage;

ALTER TABLE message
    RENAME COLUMN eco_acelleration TO corner_acceleration;

ALTER TABLE message
    RENAME COLUMN eco_brake TO acceleration_up;

ALTER TABLE message
    RENAME COLUMN eco_cornering TO acceleration_down;




