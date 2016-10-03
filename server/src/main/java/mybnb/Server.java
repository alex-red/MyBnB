package mybnb;
/**
 * Created by alexl on 7/11/2016.
 */
import static spark.Spark.*;

import com.mysql.jdbc.StringUtils;
import org.sql2o.*;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.UUID;
import com.google.gson.Gson;

public class Server {

    private static Sql2o sql2o;
    private static String dbHost = "192.168.5.5";
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(5000);
        staticFiles.location("/public");

        Sql2o sql2o = new Sql2o("jdbc:mysql://" + dbHost + ":3306/mybnb", "root", "root");
//        get("/", (req, res) -> null);

        Model model = new SqlModel(sql2o);

        get("/addresses", (req, res) -> {
            return model.getAllAddresses();
        }, new JsonTransformer());

        // Retrieve one specific address
        get("/addresses/:id", (req, res) -> {
            String id = req.params(":id");
            return model.getAddress(id);
        }, new JsonTransformer());

        // Retrieve one specific address by owner
        get("/addresses/owner/:id", (req, res) -> {
            String id = req.params(":id");
            return model.getAddressByOwner(id);
        }, new JsonTransformer());

        // Retrieve ALL listings
        get("/listings", (req, res) -> {
            return model.getAllListings();
        }, new JsonTransformer());

        post("/listings/geo", (req, res) -> {
            String lat = req.queryMap("data").get("lat").value();
            String lng = req.queryMap("data").get("lng").value();
            String miles = req.queryMap("data").get("miles").value();
            String price_min = req.queryMap("data").get("price_min").value();
            String price_max = req.queryMap("data").get("price_max").value();
            System.out.println(lat);
            return model.searchListingsByGeo(lat, lng, miles, price_min, price_max);
        }, new JsonTransformer());

        post("/listings/add", (req, res) -> {
            String address_id = req.queryMap("data").get("address_id").value();
            String user_id = req.queryMap("data").get("user_id").value();
            String amenities = req.queryMap("data").get("amenities").value();
            String available_dates = req.queryMap("data").get("available_dates").value();
            String description = req.queryMap("data").get("description").value();
            String price = req.queryMap("data").get("user_price").value();
            String room_type = "Apartment";
            String rooms = "2";
            String date = req.queryMap("data").get("date").value();
            String title = req.queryMap("data").get("title").value();
            return model.addListing(address_id, user_id, amenities, available_dates, "[]", description, price, room_type, rooms, date, title);
        }, new JsonTransformer());

        // Retrieve specified listing
        get("/listing/:id", (req, res) -> {
            String id = req.params(":id");
            return model.getListing(id);
        }, new JsonTransformer());

        // Retrieve ALL users
        get("/users", (req, res) -> {
            return model.getAllUsers();
        }, new JsonTransformer());

        // Retrieve one specific user
        get("/users/:id", (req, res) -> {
            String id = req.params(":id");
            return model.getUser(id);
        }, new JsonTransformer());

        // Retrieve all comments
        get("/comments", (req, res) -> {
            return model.getAllComments();
        }, new JsonTransformer());

        post("/comment", (req, res) -> {
            String user_id = req.queryMap("data").get("user_id").value();
            String context = req.queryMap("data").get("context").value();
            String context_id = req.queryMap("data").get("context_id").value();
            String comment = req.queryMap("data").get("comment").value();
            return model.addComment(context, context_id, user_id, comment);
        }, new JsonTransformer());

        // Update a users information
        post("/users/update/:id", (req, res) -> {
            String user_id = req.params(":id");
            String name = req.queryMap("data").get("user_name").value();
            String dob = req.queryMap("data").get("user_dob").value();
            String occupation = req.queryMap("data").get("user_occupation").value();
            String SIN = req.queryMap("data").get("user_sin").value();

            return model.updateUser(user_id, name, dob, occupation, SIN);
        }, new JsonTransformer());

        // Register new user
        post("/users/signup", (req, res) -> {
            String email = req.queryMap("data").get("user_email").value();
            String password = req.queryMap("data").get("user_password").value();
            String name = req.queryMap("data").get("user_name").value();
            String address = req.queryMap("data").get("user_address").value();
            String dob = req.queryMap("data").get("user_dob").value();
            String city = req.queryMap("data").get("user_city").value();
            String country = req.queryMap("data").get("user_country").value();
            String zipcode = req.queryMap("data").get("user_zipcode").value();
            String occupation = req.queryMap("data").get("user_occupation").value();
            String SIN = req.queryMap("data").get("user_sin").value();
            String coordinates = "[40, -79]";

            List<HashMap> user_id = model.addUser(email, password, name, dob, occupation, SIN);
            model.addAddress(model.unwrapUserId(user_id), address, coordinates, city, country, zipcode);
            return user_id;
        }, new JsonTransformer());

        // Add new address
        post("/addresses/add", (req, res) -> {
            String user_id = req.queryMap("data").get("user_id").value();
            String address = req.queryMap("data").get("user_address").value();
            String city = req.queryMap("data").get("user_city").value();
            String country = req.queryMap("data").get("user_country").value();
            String zipcode = req.queryMap("data").get("user_zipcode").value();
            String coordinates = req.queryMap("data").get("coordinates").value();
            if (coordinates.equals(null)) {
                coordinates = "['40', '-79']";
            }
            String address_id = model.addAddress(user_id, address, coordinates, city, country, zipcode);
            return address_id;
        }, new JsonTransformer());
        // Login route, returns user_id if valid email and password
        post("/login", (req, res) -> {
            String email = req.queryMap("data").get("user_email").value();
            String password = req.queryMap("data").get("user_password").value();
            return model.loginUser(email, password);
        }, new JsonTransformer());

        get("/credit_cards/:id", (req, res) -> {
            String user_id = req.params(":id");
            return model.getCards(user_id);
        }, new JsonTransformer());

        post("/credit_cards", (req, res) -> {
            String user_id = req.queryMap("data").get("user_id").value();
            String number = req.queryMap("data").get("card_number").value();
            String cvc = req.queryMap("data").get("card_cvc").value();
            String expiry = req.queryMap("data").get("card_expiry").value();
            String name = req.queryMap("data").get("card_name").value();
            return model.addCard(user_id, number, cvc, expiry, name);
        }, new JsonTransformer());

        post("/bookings", (req, res) -> {
            String host_id = req.queryMap("data").get("host_id").value();
            String renter_id = req.queryMap("data").get("renter_id").value();
            String listing_id = req.queryMap("data").get("listing_id").value();
            String available_dates = req.queryMap("data").get("available_dates").value();
            String price = req.queryMap("data").get("price").value();
            String days = req.queryMap("data").get("days").value();
            return model.addBooking(host_id, renter_id, listing_id, available_dates, price, days);
        }, new JsonTransformer());

        get("/bookings/:id", (req, res) -> {
            String id = req.params(":id");
            return model.getBookings(id);
        }, new JsonTransformer());

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });
    }

}