INSERT INTO app_user(id, name)
VALUES(25551, 'user_1'),
      (25552, 'user_2'),
      (25553, 'user_3'),
      (25554, 'user_4'),
      (25555, 'user_5');

INSERT INTO unit(id, name, user_id)
VALUES(25551, 'unit_a', 25551),
      (25552, 'unit_b', 25552),
      (25553, 'unit_c', 25552),
      (25554, 'unit_d', 25553);

INSERT INTO device(id, imei, phone_number, type, unit_id)
VALUES(25551, '355234055650192', '+37257063997', 'TRACKER', 25551),
      (25552, '355026070842667', '+3197011460885', 'TRACKER', 25551),
      (25553, '355026070834532', '+3197011405848', 'TRACKER', 25553),
      (25554, '355026070840380', '37257591012', 'BEACON', 25553),
      (25555, '358021082591268', '37257591222', 'BEACON', 25553);




