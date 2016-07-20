USE mybnb;

set @uid=UUID();
INSERT INTO users (user_id, name, address, dob, occupation, SIN) values (
  @uid, 'admin', '99 Fake Street', '1993-05-12', 'Administrator', '51232545' 
);

INSERT INTO auth (email, password, user_id) values (
  'admin@admin.com', 'admin', @uid
);

set @uid=UUID();
INSERT INTO users (user_id, name, address, dob, occupation, SIN) values (
  @uid, 'Jon Snow', '130 Winterfell', '2004-05-06', 'Lord Commander', '41232545' 
);

INSERT INTO auth (email, password, user_id) values (
  'jonsnow@jonsnow.com', '123', @uid
);