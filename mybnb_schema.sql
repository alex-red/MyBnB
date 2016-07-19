CREATE DATABASE IF NOT EXISTS mybnb;
USE mybnb;

DROP TABLE IF EXISTS users, listings, calendar, bookings, credit_cards;

CREATE TABLE users (
  user_id BINARY(16),
  PRIMARY KEY(user_id),
  name text not null,
  address text,
  dob date not null,
  occupation text,
  SIN text
);

CREATE TABLE listings (
  listing_id BINARY(16),
  PRIMARY KEY(listing_id),
  user_id BINARY(16) references users(user_id),
  title text not null,
  description text not null,
  price text not null,
  date date,
  available bool not null,
  available_dates JSON,
  comments JSON
);

CREATE TABLE calendar (
  calendar_id BINARY(16),
  PRIMARY KEY(calendar_id),
  date date not null,
  listing_id BINARY(16) references listings(listing_id)
);

CREATE TABLE bookings (
  booking_id BINARY(16),
  PRIMARY KEY(booking_id),
  host_id BINARY(16) references users(user_id),
  renter_id BINARY(16) references users(user_id),
  listing_id BINARY(16) references listings(listing_id),
  booking_date date not null,
  cancelled bool not null
);

CREATE TABLE credit_cards (
  card_id BINARY(16),
  PRIMARY KEY(card_id),
  user_id BINARY(16) references users(user_id),
  card_number text not null,
  card_cvc text not null,
  card_expiry text not null
);