package mybnb;

import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.*;

/**
 * Created by alexl on 7/21/2016.
 */
public class SqlModel implements Model {

    private Sql2o sql2o;

    public SqlModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public String addAddress(String owner_id, String address, String coordinates, String city, String country, String zipcode) {
        String sql = "INSERT INTO addresses " +
                "(address_id, owner_id, address, coordinates, city, country, zipcode) " +
                "VALUES (:address_id, :owner_id, :address, cast(:coordinates as JSON), :city, :country, :zipcode)";
        String address_id = UUID.randomUUID().toString();
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("address_id", address_id)
                    .addParameter("owner_id", owner_id)
                    .addParameter("address", address)
                    .addParameter("coordinates", coordinates)
                    .addParameter("city", city)
                    .addParameter("country", country)
                    .addParameter("zipcode", zipcode)
                    .executeUpdate();
            return address_id;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List addUser(String email, String password, String name, String dob, String occupation, String SIN) {
        String sqlAuth = "INSERT INTO auth " +
                "(email, password, user_id) " +
                "VALUES (:email, :password, :user_id)";
        String sqlUsers = "INSERT INTO users " +
                "(user_id, name, dob, occupation, SIN) " +
                "VALUES (:user_id, :name, :dob, :occupation, :SIN)";
        String user_id = UUID.randomUUID().toString();

        try (Connection con = sql2o.beginTransaction()) {
            con.createQuery(sqlAuth)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
            con.createQuery(sqlUsers)
                    .addParameter("user_id", user_id)
                    .addParameter("name", name)
                    .addParameter("dob", dob)
                    .addParameter("occupation", occupation)
                    .addParameter("SIN", SIN)
                    .executeUpdate();
            con.commit();
            return wrapUserId(user_id);
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Returns user_id hashmap in list form, i.e: [{user_id: user_id}]
    @Override
    public List wrapUserId(String user_id) {
        List<HashMap> out = new ArrayList<>();
        HashMap<String, String> user_id_obj = new HashMap<>();
        user_id_obj.put("user_id", user_id);
        out.add(user_id_obj);
        return out;
    }

    // Returns user_id string given List userid object
    @Override
    public String unwrapUserId(List<HashMap> user_id_obj) {
        HashMap<String, String> user_id_hash = user_id_obj.get(0);
        return user_id_hash.get("user_id");
    }


    @Override
    public List loginUser(String email, String password) {
        String sql = "SELECT user_id " +
                "FROM auth " +
                "WHERE email=:email AND password=:password";
        if (!StringUtils.isNullOrEmpty(email) && !StringUtils.isNullOrEmpty(password)) {
            try (Connection con = sql2o.open()) {
                return con.createQuery(sql)
                        .addParameter("email", email)
                        .addParameter("password", password)
                        .executeAndFetch(Auth.class);
            } catch (Sql2oException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Boolean updateLogin(String email, String password, String id) {
        String sql = "UPDATE auth " +
                "SET email=:email, password=:password " +
                "WHERE user_id=:user_id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .addParameter("user_id", id)
                    .executeUpdate();
            return true;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public Boolean updateUser(String id, String name, String dob, String occupation, String SIN) {
        String sqlUsers = "UPDATE users " +
                "SET name=:name, dob=:dob, occupation=:occupation, SIN=:SIN " +
                "WHERE user_id=:user_id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sqlUsers)
                    .addParameter("name", name)
                    .addParameter("dob", dob)
                    .addParameter("occupation", occupation)
                    .addParameter("SIN", SIN)
                    .addParameter("user_id", id)
                    .executeUpdate();
            return true;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public List getUser(String id) {
        String sql = "SELECT * FROM users " +
                "WHERE user_id=:user_id";
        System.out.println(id);
        try (Connection con = sql2o.open()) {
            List out = con.createQuery(sql)
                    .addParameter("user_id", id)
                    .executeAndFetch(User.class);
            System.out.println(out);
            return out;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List getAllUsers() {
        String sql = "SELECT * FROM users";
        try (Connection con = sql2o.open()) {
            List<User> users = con.createQuery(sql)
                    .executeAndFetch(User.class);
            return users;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List getAllListings() {
        String sql = "SELECT * FROM listings";
        try (Connection con = sql2o.open()) {
            List<Listing> listing = con.createQuery(sql)
                    .executeAndFetch(Listing.class);
            return listing;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List getListing(String id) {
        String sql = "SELECT * FROM listings " +
                "WHERE listing_id=:id";
        try (Connection con = sql2o.open()) {
            List<Listing> listings = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetch(Listing.class);
            return listings;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List searchListingsByGeo(String lat, String lng, String miles, String price_min, String price_max) {
        String sql = "SELECT * FROM listings NATURAL JOIN (SELECT address_id, ( 3959 * acos( cos( radians(:lat) ) * cos( radians( cast(JSON_EXTRACT(coordinates, '$[0]') AS DECIMAL(15,4)) ) )" +
                " * cos( radians( cast(JSON_EXTRACT(coordinates, '$[1]') AS DECIMAL(15,4)) ) - radians(:lng) ) + sin( radians(:lat) ) * sin( radians( cast(JSON_EXTRACT(coordinates, '$[0]')" +
                " AS DECIMAL(15,4)) ) ) ) ) AS distance FROM addresses HAVING distance < :miles ORDER BY distance) as geo_address " +
                "WHERE price >= :price_min AND price <= :price_max";
        try (Connection con = sql2o.open()) {
            List<ListingGeo> listings = con.createQuery(sql)
                    .addParameter("lat", lat)
                    .addParameter("lng", lng)
                    .addParameter("miles", miles)
                    .addParameter("price_min", price_min)
                    .addParameter("price_max", price_max)
                    .executeAndFetch(ListingGeo.class);
            return listings;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean updateListing(String listing_id, String address_id, String user_id, String amenities,
                                 String available_dates, String comments, String description, String price,
                                 String room_type, String rooms, String date, String title) {
        String sql = "UPDATE listings " +
                "SET address_id=:address_id, amenities=:amenities, available_dates=:available_dates," +
                "comments=:comments, description=:description, price=:price, room_type=:room_type," +
                "rooms=:rooms, date=:date, title=:title " +
                "WHERE listing_id=:listing_id AND user_id=:user_id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("address_id", address_id)
                    .addParameter("amenities", amenities)
                    .addParameter("available_dates", available_dates)
                    .addParameter("comments", comments)
                    .addParameter("description", description)
                    .addParameter("price", price)
                    .addParameter("room_type", room_type)
                    .addParameter("rooms", rooms)
                    .addParameter("date", date)
                    .addParameter("title", title)
                    .addParameter("listing_id", listing_id)
                    .addParameter("user_id", user_id)
                    .executeUpdate();
            return true;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public String addListing(String address_id, String user_id, String amenities,
                             String available_dates, String comments, String description, String price,
                             String room_type, String rooms, String date, String title) {
        String sql = "INSERT INTO listings (listing_id, address_id, user_id, amenities, available_dates, comments, " +
                "description, price, room_type, rooms, date, title) " +
                "VALUES (:listing_id, :address_id, :user_id, :amenities, " +
                ":available_dates, :comments, :description, " +
                ":price, :room_type, :rooms, :date, :title)";
        String listing_id = UUID.randomUUID().toString();
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("listing_id", listing_id)
                    .addParameter("address_id", address_id)
                    .addParameter("user_id", user_id)
                    .addParameter("amenities", amenities)
                    .addParameter("available_dates", available_dates)
                    .addParameter("comments", comments)
                    .addParameter("description", description)
                    .addParameter("price", price)
                    .addParameter("room_type", room_type)
                    .addParameter("rooms", rooms)
                    .addParameter("date", date)
                    .addParameter("title", title)
                    .executeUpdate();
            return listing_id;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List getAllAddresses() {
        String sql = "SELECT * FROM addresses";
        try (Connection con = sql2o.open()) {
            List<Address> addresses = con.createQuery(sql)
                    .executeAndFetch(Address.class);
            return addresses;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List getAddress(String id) {
        String sql = "SELECT * FROM addresses " +
                "WHERE address_id=:address_id";
        try (Connection con = sql2o.open()) {
            List<Address> address = con.createQuery(sql)
                    .addParameter("address_id", id)
                    .executeAndFetch(Address.class);
            return address;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List getAddressByOwner(String id) {
        String sql = "SELECT * FROM addresses " +
                "WHERE owner_id=:owner_id";
        try (Connection con = sql2o.open()) {
            List<Address> ownerAddress = con.createQuery(sql)
                    .addParameter("owner_id", id)
                    .executeAndFetch(Address.class);
            return ownerAddress;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean updateAddress(String owner_id, String address, String coordinates,
                                 String city, String country, String zipcode) {
        String sql = "UPDATE addresses " +
                "SET address=:address, coordinates=:coordinates, city=:city, " +
                "country=:country, zipcode=:zipcode " +
                "WHERE owner_id=:owner_id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("address", address)
                    .addParameter("coordinates", coordinates)
                    .addParameter("city", city)
                    .addParameter("country", country)
                    .addParameter("zipcode", zipcode)
                    .addParameter("owner_id", owner_id)
                    .executeUpdate();
            return true;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List getAllComments() {
        String sql = "SELECT * FROM comments";
        try (Connection con = sql2o.open()) {
            List<Comment> comments = con.createQuery(sql)
                    .executeAndFetch(Comment.class);
            return comments;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String addComment(String context, String context_id, String user_id, String comment) {
        String commentSql = "INSERT INTO comments (comment_id, user_id, comment) " +
                "VALUES (:comment_id, :user_id, :comment)";
        String comment_id = UUID.randomUUID().toString();
        String sql;
        if (context.equals("listing")) {
            sql = "UPDATE listings " +
                    "SET comments = JSON_ARRAY_APPEND(if(comments IS NULL, JSON_ARRAY(), comments), '$', :comment_id) " +
                    "WHERE listing_id=:context_id";
        } else {
            sql = "UPDATE users " +
                    "SET comments = JSON_ARRAY_APPEND(if(comments IS NULL, JSON_ARRAY(), comments), '$', :comment_id) " +
                    "WHERE user_id=:context_id";
        }
        try (Connection con = sql2o.beginTransaction()) {
            con.createQuery(sql)
                    .addParameter("comment_id", comment_id)
                    .addParameter("context_id", context_id)
                    .executeUpdate();
            con.createQuery(commentSql)
                    .addParameter("comment_id", comment_id)
                    .addParameter("user_id", user_id)
                    .addParameter("comment", comment)
                    .executeUpdate();
            con.commit();
            return comment_id;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List getCards(String user_id) {
        String sql = "SELECT * FROM credit_cards " +
                "WHERE user_id=:user_id";
        try (Connection con = sql2o.open()) {
            List<CreditCard> cards = con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .executeAndFetch(CreditCard.class);
            return cards;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean addCard(String user_id, String number, String cvc, String expiry, String name) {
        String sql = "INSERT INTO credit_cards (user_id, card_number, card_cvc, card_expiry, card_name) " +
                "VALUES (:user_id, :number, :cvc, :expiry, :name)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("user_id", user_id)
                    .addParameter("number", number)
                    .addParameter("cvc", cvc)
                    .addParameter("expiry", expiry)
                    .addParameter("name", name)
                    .executeUpdate();
            return true;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List getBookings(String id) {
        String sql = "SELECT * FROM bookings " +
                "WHERE renter_id=:id OR host_id=:id";
        try (Connection con = sql2o.open()) {
            List<Booking> bookings = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetch(Booking.class);
            return bookings;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String addBooking(String host_id, String renter_id, String listing_id, String available_dates, String price, String days) {
        String sql = "INSERT INTO bookings (booking_id, host_id, renter_id, listing_id, available_dates, price, days) " +
                "VALUES (:booking_id, :host_id, :renter_id, :listing_id, :available_dates, :price, :days)";
        String booking_id = UUID.randomUUID().toString();
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("booking_id", booking_id)
                    .addParameter("host_id", host_id)
                    .addParameter("renter_id", renter_id)
                    .addParameter("listing_id", listing_id)
                    .addParameter("available_dates", available_dates)
                    .addParameter("price", price)
                    .addParameter("days", days)
                    .executeUpdate();
            return booking_id;
        } catch (Sql2oException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
//
//
//
//    @Override
//    public Boolean ReportOne(String date_min, String date_max) {
////        Total number of bookings in a specific date range
//        String report = "SELECT COUNT(address) AS number_of_bookings " +
//                "FROM addresses WHERE address_id = " +
//                "(SELECT address_id FROM bookings WHERE " +
//                ":date_min <= cast(JSON_EXTRACT(available_dates, '$[0]') AS DATE) AND " +
//                ":date_max >= cast(JSON_EXTRACT(available_dates, '$[0]') AS DATE)) AS add";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportTwo(String zipcode) {
////        Total number of bookings  by zipcode
//        String report = "SELECT COUNT(address) AS number_of_bookings " +
//                "FROM addresses WHERE address_id = " +
//                "(SELECT address_id FROM bookings WHERE " +
//                "zipcode = :zipcode) AS add";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportThree(String country, String city, String zipcode) {
////        Total number of listings per country, city, postal code
//        String report = "SELECT COUNT(address) AS number_of_listings " +
//                "FROM addresses WHERE address_id = " +
//                "(SELECT address_id FROM listings) AS add" +
//                "AND country = :country " +
//                "AND city = :city " +
//                "AND zipcode = :zipcode";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportFour(String country, String city) {
////        Total number of listings per country, city
//        String report = "SELECT COUNT(address) AS number_of_listings " +
//                "FROM addresses WHERE address_id = " +
//                "(SELECT address_id FROM listings) AS add" +
//                "AND country = :country " +
//                "AND city = :city";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportFive(String country) {
////        Total number of listings per country
//        String report = "SELECT COUNT(address) AS number_of_listings " +
//                "FROM addresses WHERE address_id = " +
//                "(SELECT address_id FROM listings) AS add" +
//                "AND country = :country";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportSix(String country) {
////        Total number of listings ranked by user and country
//        String report = "SELECT owner_id, country, COUNT(owner_id) AS number_of_listings " +
//                "FROM addresses WHERE address_id = " +
//                "(SELECT address_id FROM listings) AS add" +
//                "AND country = :country" +
//                "ORDER by number_of_listings";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportEight() {
////        Hosts that have more than 10% of the number of listings
//        String report = "SELECT owner_id FROM " +
//                "(SELECT COUNT(listing_id) FROM listings" +
//                "WHERE address_id = (SELECT address_id FROM addresses) AS add " +
//                "GROUP BY add.country, add.city) as user_listings" +
//                "WHERE user_listings > (SELECT COUNT(listing_id) FROM listings) * 0.10 " +
//                "AS total_listings)";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportNine(String date_min, String date_max) {
////        Renters by specific time period
//        String report = "SELECT renter_id, COUNT(renter_id) as num_of_bookings FROM " +
//                "(SELECT renter_id FROM bookings AS b WHERE" +
//                ":date_min <= cast(JSON_EXTRACT(b.available_dates, '$[0]') AS DATE) AND" +
//                ":date_max >= cast(JSON_EXTRACT(b.available_dates, '$[0]') AS DATE) AS renters " +
//                "ORDER by num_of_bookings";
//        return true;
//    }
//
//
//    @Override
//    public Boolean ReportTen(String date) {
////        Hosts with most cancellations in a year
////        date = the last day of the year you want to check
//        String report = "SELECT b.host_id, cancellations FROM " +
//                "bookings AS b, (SELECT COUNT(booking_id) FROM bookings " +
//                "WHERE status = 'cancelled') as cancellations " +
//                "WHERE cast(JSON_EXTRACT(available_dates, '$[0]') AS DATE) <= :date";
//        return true;
//    }
//
//    @Override
//    public Boolean ReportEleven(String date) {
////        Renters  with most cancellations in a year
////        date = the last day of the year you want to check
//        String report = "SELECT b.renter_id, cancellations FROM " +
//                "bookings AS b, (SELECT COUNT(booking_id) FROM bookings " +
//                "WHERE status = 'cancelled') as cancellations " +
//                "WHERE cast(JSON_EXTRACT(available_dates, '$[0]') AS DATE) <= :date";
//        return true;
//    }
//
}
