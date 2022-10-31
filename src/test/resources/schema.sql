CREATE TABLE app_user
(
    id      SERIAL      NOT NULL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    deleted BOOLEAN     NOT NULL DEFAULT false
);

CREATE UNIQUE INDEX unique_name_not_deleted_users
    ON app_user (name) WHERE (deleted = false);

CREATE TABLE unit
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    user_id INTEGER     NOT NULL REFERENCES app_user,
    deleted BOOLEAN     NOT NULL DEFAULT false
);

CREATE UNIQUE INDEX unique_name_not_deleted_units
    ON unit (name) WHERE (deleted = false);

CREATE TABLE device
(
    id           SERIAL PRIMARY KEY,
    imei         VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    type         VARCHAR(64) NOT NULL,
    unit_id      INTEGER     NOT NULL REFERENCES unit,
    deleted      BOOLEAN     NOT NULL DEFAULT false
);

CREATE UNIQUE INDEX unique_imei_not_deleted_device
    ON device (imei) WHERE (deleted = false);

ALTER TABLE device
    ADD CONSTRAINT correct_type CHECK (type IN ('TRACKER', 'BEACON'));

CREATE TABLE command
(
    id        BIGSERIAL PRIMARY KEY,
    text      TEXT        NOT NULL,
    status    VARCHAR(64) NOT NULL,
    device_id INTEGER     NOT NULL REFERENCES device,
    type      VARCHAR(64) NOT NULL
);

ALTER TABLE command
    ADD CONSTRAINT valid_command_status
        CHECK (status IN ('NEW', 'SENT', 'SUCCESS', 'ERROR', 'TIMEOUT'));

ALTER TABLE command
    ADD CONSTRAINT valid_command_type
        CHECK (type IN ('COMMAND', 'ANSWER'));

CREATE TABLE message
(
    id               BIGSERIAL PRIMARY KEY,
    device_id        INTEGER      NOT NULL REFERENCES device,
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
    deleted          BOOLEAN      NOT NULL DEFAULT false
);









