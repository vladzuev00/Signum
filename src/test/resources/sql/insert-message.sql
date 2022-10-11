INSERT INTO app_user(id, name) VALUES(1, 'vladzuev');
INSERT INTO tracker(id, imei, phone_number) VALUES(1, '11111222223333344444', '448447045');
INSERT INTO unit(id, name, user_id, tracker_id) VALUES(1, 'unit-name', 1, 1);
INSERT INTO message(id, unit_id, time, latitude, longitude, speed, course, altitude, amount_satellite, hdop, params)
VALUES(1, 1, '2000-02-18 04:05:06', 4.4, 5.5, 10, 11, 12, 13, 6.6, 'param1:value1,param2:value2');
