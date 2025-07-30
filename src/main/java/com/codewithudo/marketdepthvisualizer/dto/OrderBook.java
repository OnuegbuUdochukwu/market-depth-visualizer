package com.codewithudo.marketdepthvisualizer.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderBook {

    private List<Order> asks;
    private List<Order> bids;

}