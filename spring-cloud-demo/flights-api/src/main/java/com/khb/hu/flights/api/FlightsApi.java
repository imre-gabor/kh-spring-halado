package com.khb.hu.flights.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.khb.hu.flights.dto.Airline;

@FeignClient(name = "flights", url="${feign.flights.url}")
public interface FlightsApi {

    @GetMapping("/flight/{from}/{to}")
    List<Airline> searchFlight(@PathVariable("from") String from, @PathVariable("to") String to);

}