package com.codewithudo.marketdepthvisualizer;

import com.codewithudo.marketdepthvisualizer.dto.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonTest {

    public static void main(String[] args) {
        // This is a sample of the JSON data for the 'asks' or 'bids' array from Quidax
        String jsonArrayOfOrders = "[[\"180598.0\", \"0.664\"], [\"180581.0\", \"2.179\"]]";

        System.out.println("Attempting to parse the following JSON:");
        System.out.println(jsonArrayOfOrders);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // We are trying to convert the JSON string into a List of our Order objects
            List<Order> orders = objectMapper.readValue(jsonArrayOfOrders, new TypeReference<>() {});

            System.out.println("\nSUCCESS! Parsed successfully.");
            System.out.println("Number of orders parsed: " + orders.size());
            System.out.println("First order price: " + orders.get(0).getPrice());
            System.out.println("First order volume: " + orders.get(0).getVolume());

        } catch (Exception e) {
            System.err.println("\nFAILURE! The test failed with an exception:");
            e.printStackTrace();
        }
    }
}