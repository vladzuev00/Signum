INSERT INTO app_user(id, name)
VALUES(1, 'user_1'),
      (2, 'user_2'),
      (3, 'user_3'),
      (4, 'user_4'),
      (5, 'user_5');

INSERT INTO tracker(id, imei, phone_number)
VALUES(1, '355234055650192', '+37257063997'),
      (2, '355026070842667', '+3197011460885'),
      (3, '355026070834532', '+3197011405848'),
      (4, '355026070840380', '37257591012'),
      (5, '358021082591268', '37257591222');

INSERT INTO unit(id, name, user_id, tracker_id)
VALUES(1, 'unit_a', 1, 1),
      (2, 'unit_b', 2, 2),
      (3, 'unit_c', 2, 3),
      (4, 'unit_d', 3, 4);




