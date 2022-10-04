CREATE TABLE users(
                      id SERIAL NOT NULL PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      delete BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE units(
                      id SERIAL NOT NULL PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      user_id INTEGER NOT NULL,
                      tracker_id INTEGER NOT NULL,
                      delete BOOLEAN NOT NULL DEFAULT false

);

CREATE TABLE trackers(
                         id SERIAL NOT NULL PRIMARY KEY,
                         imei VARCHAR(20) NOT NULL,
                         phone_number VARCHAR(20) NOT NULL,
                         deleted BOOLEAN NOT NULL DEFAULT false
);

ALTER TABLE units
    ADD CONSTRAINT fk_units_to_users FOREIGN KEY(user_id) REFERENCES users(id);

ALTER TABLE units
    ADD CONSTRAINT fk_units_to_trackers FOREIGN KEY(tracker_id) REFERENCES trackers(id);

ALTER TABLE units
    ADD CONSTRAINT unique_tracker_id UNIQUE (tracker_id);

CREATE TABLE messages(
                         id BIGSERIAL NOT NULL PRIMARY KEY,
                         tracker_id INTEGER NOT NULL,
                         datetime TIMESTAMP(0) NOT NULL,
                         latitude FLOAT(4) NOT NULL,
                         longitude FLOAT(4) NOT NULL,
                         speed SMALLINT NOT NULL,
                         course SMALLINT NOT NULL,
                         altitude SMALLINT NOT NULL,
                         amount_satellite SMALLINT NOT NULL,
                         hdop FLOAT(4) NOT NULL,
                         params TEXT NOT NULL,
                         delete BOOLEAN NOT NULL DEFAULT false
);

ALTER TABLE messages
    ADD CONSTRAINT fk_messages_to_trackers FOREIGN KEY(tracker_id) REFERENCES trackers(id);
