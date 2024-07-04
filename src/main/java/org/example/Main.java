package org.example;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<Holiday> holidays = new ArrayList<>();
    public static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        loadholidays();
        System.out.println(holidays);
//        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
//        server.createContext("/createHolidays",new UserHandler());
//        server.createContext("/getHolidays",new UserHandler());
//        server.createContext("/getHoliday",new UserHandler());
//        server.createContext("/updateHoliday",new UserHandler());
//        server.createContext("/deleteHoliday",new UserHandler());
//
//        server.setExecutor(null);
//        server.start();
    }

    public static void loadholidays() {
        try (FileReader reader = new FileReader("holidays.json")) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                long id = jsonObject.get("id").getAsLong();
                String title = jsonObject.get("title").getAsString();
                String country = jsonObject.get("country").getAsString();
                String city = jsonObject.get("city").getAsString();
                String duration = jsonObject.get("duration").getAsString();
                String season = jsonObject.get("season").getAsString();
                String description = jsonObject.get("description").getAsString();
                double price = jsonObject.get("price").getAsDouble();
                JsonArray photosJsonArray = jsonObject.getAsJsonArray("photos");
                String[] photos = new String[photosJsonArray.size()];
                for (int i = 0; i < photosJsonArray.size(); i++) {
                    photos[i] = photosJsonArray.get(i).getAsString();
                }

                JsonArray ratingJsonArray = jsonObject.getAsJsonArray("rating");
                int[] rating = new int[ratingJsonArray.size()];
                for (int i = 0; i < ratingJsonArray.size(); i++) {
                    rating[i] = ratingJsonArray.get(i).getAsInt();
                }

                Holiday holiday = new Holiday();
                holiday.setId(id);
                holiday.setTitle(title);
                holiday.setCountry(country);
                holiday.setCity(city);
                holiday.setDuration(duration);
                holiday.setSeason(season);
                holiday.setDescription(description);
                holiday.setPrice(price);
                holiday.setPhotos(photos);
                holiday.setRating(rating);
                holidays.add(holiday);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void saveHoliday() {
        try (FileWriter writer = new FileWriter("holidays.json")) {
            gson.toJson(holidays, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}