package org.example;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static List<holiday> holidays = new ArrayList<>();
    public static Gson gson = new Gson();
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static void saveUsers(){
        try (FileWriter writer = new FileWriter("holidays.json")){
            gson.toJson(holidays,writer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}