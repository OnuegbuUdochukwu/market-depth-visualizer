package com.codewithudo.marketdepthvisualizer.service;

import com.codewithudo.marketdepthvisualizer.dto.DepthResponse;
import com.codewithudo.marketdepthvisualizer.dto.OrderBook;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DepthService {

    private final RestTemplate restTemplate;

    public DepthService() {
        this.restTemplate = new RestTemplate();
    }

    public OrderBook getOrderBook(String market) {
        String url = "https://app.quidax.com/api/v1/markets/" + market + "/depth";

        DepthResponse response = restTemplate.getForObject(url, DepthResponse.class);

        if (response != null && "success".equals(response.getStatus())) {
            return response.getData();
        }

        return null;
    }
}