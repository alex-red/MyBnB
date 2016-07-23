CREATE DATABASE IF NOT EXISTS mybnb;

USE mybnb;

DROP TABLE IF EXISTS users, auth, addresses, listings, calendar, comments, bookings, credit_cards;

CREATE TABLE users (
  user_id char(36) not null,
  PRIMARY KEY(user_id),
  name varchar(200) not null,
  dob date not null,
  occupation varchar(200),
  SIN char(9)
);

CREATE TABLE auth (
  email varchar(200) not null,
  PRIMARY KEY(email),
  password varchar(200) not null,
  user_id char(36) not null references users(user_id)
);

CREATE TABLE addresses (
  address_id char(36) not null,
  PRIMARY KEY(address_id),
  owner_id char(36) not null references users(user_id),
  address varchar(200) not null,
  coordinates JSON,
  city varchar(100) not null,
  country varchar(100) not null,
  zipcode varchar(32) not null
);

CREATE TABLE listings (
  listing_id char(36) not null,
  PRIMARY KEY(listing_id),
  address_id char(36) references addresses(address_id),
  user_id char(36) references users(user_id),
  amenities JSON,
  available_dates JSON,
  comments JSON,
  description varchar(200) not null,
  price varchar(32) not null,
  room_type varchar(10) not null,
  rooms varchar(10) not null,
  date date,
  title varchar(200) not null
);

CREATE TABLE calendar (
  date date not null,
  PRIMARY KEY(date),
  listings JSON
);

CREATE TABLE comments (
  comment_id char(36) not null,
  user_id char(36) references users(user_id),
  PRIMARY KEY(comment_id, user_id),
  comment varchar(500) not null
);

CREATE TABLE bookings (
  booking_id char(36) not null,
  PRIMARY KEY(booking_id),
  host_id char(36) references users(user_id),
  renter_id char(36) references users(user_id),
  listing_id char(36) references listings(listing_id),
  booking_date date not null,
  status varchar(10) not null
);

CREATE TABLE credit_cards (
  card_id char(36) not null,
  PRIMARY KEY(card_id),
  user_id char(36) references users(user_id),
  card_number varchar(20)  not null,
  card_cvc varchar(10)  not null,
  card_expiry date not null
);

GRANT ALL PRIVILEGES ON *.* TO root@'%' IDENTIFIED by 'root';
FLUSH PRIVILEGES;
