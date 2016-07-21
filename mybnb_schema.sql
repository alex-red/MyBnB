CREATE DATABASE IF NOT EXISTS mybnb;

USE mybnb;

DROP TABLE IF EXISTS users, auth, listings, calendar, bookings, credit_cards;

CREATE TABLE users (
  user_id varchar(36) not null,
  PRIMARY KEY(user_id),
  name varchar(200) not null,
  address varchar(200),
  dob date not null,
  country varchar(200) not null,
  zipcode varchar(32),
  occupation varchar(200),
  SIN varchar(9)
);

CREATE TABLE auth (
  email varchar(200) not null,
  PRIMARY KEY(email),
  password varchar(200) not null,
  user_id varchar(36) references users(user_id)
);

CREATE TABLE listings (
  listing_id varchar(36) not null,
  PRIMARY KEY(listing_id),
  user_id varchar(36) references users(user_id),
  title varchar(200) not null,
  description varchar(200) not null,
  price varchar(32) not null,
  date date,
  available bool not null,
  available_dates JSON,
  comments JSON
);

CREATE TABLE calendar (
  calendar_id varchar(36) not null,
  PRIMARY KEY(calendar_id),
  date date not null,
  listing_id varchar(36) references listings(listing_id)
);

CREATE TABLE bookings (
  booking_id varchar(36) not null,
  PRIMARY KEY(booking_id),
  host_id varchar(36) references users(user_id),
  renter_id varchar(36) references users(user_id),
  listing_id varchar(36) references listings(listing_id),
  booking_date date not null,
  cancelled bool not null
);

CREATE TABLE credit_cards (
  card_id varchar(36) not null,
  PRIMARY KEY(card_id),
  user_id varchar(36) references users(user_id),
  card_number varchar(20)  not null,
  card_cvc varchar(10)  not null,
  card_expiry date not null
);

GRANT ALL PRIVILEGES ON *.* TO root@'%' IDENTIFIED by 'root';
FLUSH PRIVILEGES;
