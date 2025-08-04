package com.codewithudo.marketdepthvisualizer.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderBook {
    private long timestamp;
    private List<Order> asks;
    private List<Order> bids;
}