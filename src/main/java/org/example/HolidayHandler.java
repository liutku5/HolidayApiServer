package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.example.Main.*;

public class HolidayHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/createHolidays") && method.equals("POST")) {// +
            handleCreateHoliday(exchange);
        }
        if (path.equals("/getHolidays") && method.equals("GET")) {// +
            handleGetHolidays(exchange);
        }
        if (path.equals("/getHoliday") && method.equals("GET")) {// +
            handleGetHolidayByid(exchange);
        }
        if (path.equals("/updateHoliday") && method.equals("POST")) {// +
            handleUpdateHoliday(exchange);
        }
        if (path.equals("/deleteHoliday") && method.equals("POST")) {// +
            handleDeleteHoliday(exchange);
        }

        exchange.sendResponseHeaders(400, -1);
        OutputStream os = exchange.getResponseBody();
        os.close();
    }

    private void handleCreateHoliday(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = queryToMap(query);

        long id = Long.parseLong(params.get("id"));
        String title = params.get("title");
        String country = params.get("country");
        String city = params.get("city");
        String duration = params.get("duration");
        String season = params.get("season");
        String description = params.get("description");
        double price = Double.parseDouble(params.get("price"));
        String[] photos = new String[]{params.get("photos")};
        int[] rating = new int[]{Integer.parseInt(params.get("rating"))};


        Holiday holiday = new Holiday(id, title, country, city, duration,season,description,price,photos,rating);
        holidays.add(holiday);
        saveHoliday();
        String response = "Holiday has been created successfully";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleDeleteHoliday(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        long id = Long.parseLong(query.split("=")[1]);
        boolean removed = holidays.removeIf(u -> u.getId() == id);

        if (removed) {
            saveHoliday();
            String response = "Holiday has been deleted successfully";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    private void handleUpdateHoliday(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = queryToMap(query);

        long id = Long.parseLong(params.get("id"));
        String title = params.get("title");
        String country = params.get("country");
        String city = params.get("city");
        String duration = params.get("duration");
        String season = params.get("season");
        String description = params.get("description");
        double price = Double.parseDouble(params.get("price"));
        String[] photos = new String[]{params.get("photos")};
        int[] rating = new int[]{Integer.parseInt(params.get("rating"))};


        Holiday holiday = new Holiday(id, title, country, city, duration,season,description,price,photos,rating);
        holidays.stream()
                .filter(u -> u.getId() == holiday.getId())
                .findFirst()
                .map(existingUser -> {
                    holidays.set(holidays.indexOf(existingUser), holiday);
                    return true;
                })
                .orElseGet(() -> {
                    holidays.add(holiday);
                    return false;
                });
        saveHoliday();
        String response = "Holiday has been updated successfully";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleGetHolidays(HttpExchange exchange) throws IOException {

        String response = gson.toJson(holidays);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private void handleGetHolidayByid(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();
        long id = Long.parseLong(query.split("=")[1]);;
        Holiday holiday = holidays.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        if (holiday != null) {
            String response = gson.toJson(holiday);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(URLDecoder.decode(entry[0], StandardCharsets.UTF_8), URLDecoder.decode(entry[1], StandardCharsets.UTF_8));
            } else {
                result.put(URLDecoder.decode(entry[0], StandardCharsets.UTF_8), "");
            }
        }
        return result;
    }

}
