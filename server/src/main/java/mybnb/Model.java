package mybnb;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by alexl on 7/21/2016.
 */
public interface Model {
    List addUser(String email, String password, String name, String dob, String occupation, String SIN);
    String addAddress(String owner_id, String address, String coordinates, String city, String country, String zipcode);
    List loginUser(String email, String password);
    Boolean updateLogin(String email, String password, String id);
    Boolean updateUser(String id, String name, String dob, String occupation, String SIN);
    List getUser(String id);
    List getAllUsers();
    List getAllListings();
    List getListing(String id);
    String addListing(String address_id, String user_id, String amenities,
                      String available_dates, String comments, String description, String price,
                      String room_type, String rooms, String date, String title);
    Boolean updateListing(String listing_id, String address_id, String user_id, String amenities,
                          String available_dates, String comments, String description, String price,
                          String room_type, String rooms,  String date, String title);
    List getAddress(String id);
    List getAddressByOwner(String id);
    Boolean updateAddress(String owner_id, String address, String coordinates, String city, String country, String zipcode);
    List getAllAddresses();
    List getAllComments();
    String addComment(String context, String context_id, String user_id, String comment);
    List getCards(String user_id);
    Boolean addCard(String user_id, String number, String cvc, String expiry, String name);
    List wrapUserId(String user_id);
    List getBookings(String id);
    String addBooking(String host_id, String renter_id, String listing_id, String available_dates, String price, String days);
    List searchListingsByGeo(String lat, String lng, String miles, String price_min, String price_max);
    String unwrapUserId(List<HashMap> user_id_obj);
}

@Data
class User {
    UUID user_id;
    String name;
    Date dob;
    String occupation;
    String SIN;
    String comments;
}

@Data
class Auth {
    String email;
    String password;
    UUID user_id;
}

@Data
class Address {
    UUID address_id;
    UUID owner_id;
    String address;
    String coordinates;
    String city;
    String country;
    String zipcode;
}

@Data
class Listing {
    UUID listing_id;
    UUID address_id;
    UUID user_id;
    String amenities;
    String available_dates;
    String comments;
    String description;
    String price;
    String room_type;
    String rooms;
    Date date;
    String title;
}

@Data
class ListingGeo {
    UUID listing_id;
    UUID address_id;
    UUID user_id;
    String amenities;
    String available_dates;
    String comments;
    String description;
    String price;
    String room_type;
    String rooms;
    Date date;
    String title;
    String distance;
}

@Data
class Calendar {
    Date date;
    String listings;
}

@Data
class Comment {
    UUID comment_id;
    UUID user_id;
    String comment;
    String helpful;
}

@Data
class Booking {
    UUID booking_id;
    UUID host_id;
    UUID renter_id;
    UUID listing_id;
    String available_dates;
    String status;
    String days;
    String price;
}

@Data
class CreditCard {
    UUID user_id;
    String card_number;
    String card_cvc;
    Date card_expiry;
    String card_name;
}