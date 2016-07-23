USE mybnb;

-- USERS
set @uid1=UUID();
INSERT INTO users (user_id, name, dob, occupation, SIN) values (
  @uid1, 'admin', '1993-05-12', 'administrator', '51232545' 
);
set @aid1=UUID();
INSERT INTO addresses (address_id, owner_id, address, coordinates, city, country, zipcode) values (
  @aid1, @uid1, '99 fake street', '["43.784560", "-79.184770"]', 'toronto', 'canada', 'm1a4k5'
);
INSERT INTO auth (email, password, user_id) values (
  'admin@admin.com', 'admin', @uid1
);

set @umayor=UUID();
INSERT INTO users (user_id, name, dob, occupation, SIN) values (
  @umayor, 'mayor', '1973-05-12', 'mayor', '51232545' 
);
INSERT INTO auth (email, password, user_id) values (
  'mayor@mayor.com', 'mayor', @uid1
);

set @ujonsnow=UUID();
INSERT INTO users (user_id, name, dob, occupation, SIN) values (
  @ujonsnow, 'Jon Snow', '1990-05-06', 'Lord Commander', '41232545' 
);
set @aid2=UUID();
INSERT INTO addresses (address_id, owner_id, address, coordinates, city, country, zipcode) values (
  @aid2, @ujonsnow, '130 winterfell', '["43.784560", "-79.184770"]', 'toronto', 'canada', 'm1a4k5'
);
INSERT INTO auth (email, password, user_id) values (
  'jonsnow@jonsnow.com', '123', @ujonsnow
);

set @uyvette=UUID();
INSERT INTO users (user_id, name, dob, occupation, SIN) values (
  @uyvette, 'Yvette R Long', '1990-01-22', 'Recruitment consultant', '592347892' 
);
set @ayvette=UUID();
INSERT INTO addresses (address_id, owner_id, address, coordinates, city, country, zipcode) values (
  @ayvette, @uyvette, '3620 Robson St', '["49.2932180", "-123.1381600"]', 'vancouver', 'canada', 'V6B3K9'
);
INSERT INTO auth (email, password, user_id) values (
  'yvette.r.long@recruitment.com', 'admin', @uyvette
);

-- ADDRESSES

set @cntower=UUID();
INSERT INTO addresses (address_id, owner_id, address, coordinates, city, country, zipcode) values (
  @cntower, @umayor, '301 Front St W', '["43.6425662", "-79.3870380"]', 'toronto', 'canada', 'M5V2T6'
);

-- COMMENTS
set @comment1=UUID();
INSERT INTO comments (comment_id, user_id, comment) VALUES (
  @comment1, @ujonsnow, 'Hello'
);

-- LISTINGS
set @list_id=UUID();
INSERT INTO listings (listing_id, address_id, user_id, amenities, available_dates, comments, description, price, room_type, rooms, date, title) 
VALUES (@list_id, @aid2, @ujonsnow, '["tv", "phone", "pool", "internet"]', '["2016-07-22", "2016-09-25"]', REPLACE('["id"]', 'id', @comment1), 
  'An amazing location with historical features', "250", "house", "4", CURDATE(), "A Castle in the North"
);

set @list_id=UUID();
INSERT INTO listings (listing_id, address_id, user_id, amenities, available_dates, comments, description, price, room_type, rooms, date, title) 
VALUES (@list_id, @ayvette, @uyvette, '["tv", "phone", "pool", "internet"]', '["2016-07-22", "2016-08-23"]', REPLACE('["id"]', 'id', null), 
  'Gorgeous condo in harbourfront vancouver.', "170", "apartment", "2", CURDATE(), "Condo by Vancouver Harbour"
);

set @list_id=UUID();
INSERT INTO listings (listing_id, address_id, user_id, amenities, available_dates, comments, description, price, room_type, rooms, date, title) 
VALUES (@list_id, @cntower, @umayor, '["tv", "phone", "pool", "internet"]', '["2016-07-22", "2016-10-13"]', REPLACE('["id"]', 'id', null), 
  'Downtown toronto condo with all of the sites and amenities.', "300", "apartment", "2", CURDATE(), "Downtown TO Condo"
);
