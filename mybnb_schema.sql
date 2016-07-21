CREATE DATABASE IF NOT EXISTS mybnb;

USE mybnb;

DROP TABLE IF EXISTS users, auth, addresses, listings, calendar, bookings, credit_cards;

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
  address varchar(200) not null,
  coordinates JSON,
  country varchar(200) not null,
  zipcode varchar(32) not null
);

CREATE TABLE listings (
  listing_id char(36) not null,
  PRIMARY KEY(listing_id),
  address_id char(36) references addresses(address_id),
  available bool not null,
  available_dates JSON,
  comments JSON,
  date date,
  description varchar(200) not null,
  price varchar(32) not null,
  title varchar(200) not null,
  type varchar(10),
  user_id char(36) references users(user_id)
);

CREATE TABLE calendar (
  calendar_id char(36) not null,
  PRIMARY KEY(calendar_id),
  date date not null,
  listing_id char(36) references listings(listing_id)
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
