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
    user_id INTEGER     NOT NULL REFERENCES app_user,
    deleted BOOLEAN     NOT NULL DEFAULT false
);

CREATE TYPE device_type AS ENUM('TRACKER', 'BEACON');

CREATE TABLE device
(
    id           SERIAL PRIMARY KEY,
    imei         VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    type         device_type NOT NULL,
    unit_id      INTEGER     NOT NULL REFERENCES unit,
    deleted      BOOLEAN     NOT NULL DEFAULT false
);

CREATE TYPE command_status AS ENUM('NEW', 'SENT', 'SUCCESS', 'ERROR', 'TIMEOUT');
CREATE TYPE command_type AS ENUM('COMMAND', 'ANSWER');

CREATE TABLE command
(
    id        BIGSERIAL PRIMARY KEY,
    text      TEXT        NOT NULL,
    status    command_status NOT NULL,
    device_id INTEGER     NOT NULL REFERENCES device,
    type      command_type NOT NULL
);

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

--- UNIQUE INDEXES

CREATE UNIQUE INDEX unique_imei_not_deleted_device
    ON device (imei) WHERE (deleted = false);

CREATE UNIQUE INDEX unique_name_not_deleted_users
    ON app_user (name) WHERE (deleted = false);

CREATE UNIQUE INDEX unique_name_not_deleted_units
    ON unit (name) WHERE (deleted = false);

--------------






