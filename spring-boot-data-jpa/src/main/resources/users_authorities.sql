
INSERT INTO users (username,password,enabled) VALUES('ramiro', '$2a$10$THnLKN.HudN2/d7RI0p4O.OPCvsWZlxPL3QJIKS9qUUJoNGcJWOoa', 1);
INSERT INTO users (username,password,enabled) VALUES('admin', '$2a$10$9MfoZ.gI93BWAA/OHmzfKudnZ9/CjaPShVYgRnywct7JZiQoF.bdm', 1);


INSERT INTO authorities (user_id,authority) VALUES (1, 'ROLE_USER');
INSERT INTO authorities (user_id,authority) VALUES (2, 'ROLE_ADMIN');
INSERT INTO authorities (user_id,authority) VALUES (2, 'ROLE_USER');

