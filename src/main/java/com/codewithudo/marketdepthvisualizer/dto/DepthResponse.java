package com.codewithudo.marketdepthvisualizer.dto;

import lombok.Data;

@Data
public class DepthResponse {

    private String status;
    private OrderBook data;

}
