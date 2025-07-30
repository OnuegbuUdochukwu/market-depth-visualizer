package com.codewithudo.marketdepthvisualizer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Order {

    String price;
    String volume;

    @JsonCreator
    public Order(
            @JsonProperty("price") String price,
            @JsonProperty("volume") String volume) {
        this.price = price;
        this.volume = volume;
    }
}
