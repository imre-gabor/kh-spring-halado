package com.khb.hu.flights.web;

import com.khb.hu.flights.api.FlightsApi;
import com.khb.hu.flights.dto.Airline;
import com.khb.hu.flights.service.AirlineService;

import java.time.Duration;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasAuthority('SEARCH_FLIGHTS')")
    public List<Airline> searchFlight(String from, String to) {
//        try {
//            Thread.sleep(Duration.ofMinutes(1));
//        } catch (InterruptedException e) {
//        }
        //System.out.println(request.getHeader("x-jwt-username"));
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return airlineService.search(from, to);
    }
}
