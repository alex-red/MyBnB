USE mybnb;

set @uid=UUID();
INSERT INTO users (user_id, name, dob, occupation, SIN) values (
  @uid, 'admin', '1993-05-12', 'administrator', '51232545' 
);
set @aid=UUID();
INSERT INTO addresses (address_id, address, coordinates, country, zipcode) values (
  @aid, '99 fake street', '["43.784560", "-79.184770"]', 'canada', 'm1a4k5'
);
INSERT INTO auth (email, password, user_id) values (
  'admin@admin.com', 'admin', @uid
);

set @uid=UUID();
INSERT INTO users (user_id, name, dob, occupation, SIN) values (
  @uid, 'Jon Snow', '2004-05-06', 'Lord Commander', '41232545' 
);
set @aid=UUID();
INSERT INTO addresses (address_id, address, coordinates, country, zipcode) values (
  @aid, '130 winterfell', '["43.784560", "-79.184770"]', 'canada', 'm1a4k5'
);
INSERT INTO auth (email, password, user_id) values (
  'jonsnow@jonsnow.com', '123', @uid
);