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
                        tracker_id INTEGER NOT NULL REFERENCES tracker
);

ALTER TABLE command
    ADD CONSTRAINT valid_command_status CHECK (status IN ('DELIVERED', 'NOT_DELIVERED'));
