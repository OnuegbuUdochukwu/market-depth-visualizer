package com.codewithudo.marketdepthvisualizer.controller;

import com.codewithudo.marketdepthvisualizer.dto.OrderBook;
import com.codewithudo.marketdepthvisualizer.service.DepthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/depth")
public class DepthController {

    private final DepthService depthService;

    public DepthController(DepthService depthService) {
        this.depthService = depthService;
    }

    @GetMapping("/{market}")
    public OrderBook getOrderBookForMarket(@PathVariable String market) {
        return depthService.getOrderBook(market);
    }
}