package com.khb.hu.flights.web;

import com.khb.hu.flights.api.FlightsApi;
import com.khb.hu.flights.dto.Airline;
import com.khb.hu.flights.service.AirlineService;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FlightsController implements FlightsApi {

    @Autowired
    private AirlineService airlineService;
    
    @Autowired
    HttpServletRequest request;
    
    
    @Override
    public List<Airline> searchFlight(String from, String to) {
    	System.out.println(request.getHeader("x-jwt-username"));
        return airlineService.search(from, to);
    }
}