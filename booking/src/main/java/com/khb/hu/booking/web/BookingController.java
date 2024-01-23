package com.khb.hu.booking.web;

import java.util.Comparator;
import java.util.List;

import com.khb.hu.booking.dto.TicketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khb.hu.booking.dto.PurchaseData;
import com.khb.hu.currency.api.CurrencyApi;
import com.khb.hu.flights.api.FlightsApi;
import com.khb.hu.flights.dto.Airline;

@RestController
@RequestMapping("/api")
public class BookingController {

    private static final String STANDARD_CURRENCY = "USD";
    
    @Autowired
    FlightsApi flightsApi;
    
    @Autowired
    CurrencyApi currencyApi;

	@PostMapping("/ticket")
    public PurchaseData buyTicket(@RequestBody TicketData ticketData) {
    	PurchaseData purchaseData = new PurchaseData();
    	
    	List<Airline> foundFlights = flightsApi.searchFlight(ticketData.getFrom(), ticketData.getTo());
    	
    	if(foundFlights.isEmpty()) {
    		purchaseData.setSuccess(false);
    		return purchaseData;
    	}
    	
    	purchaseData.setSuccess(true);
    	
		foundFlights
    	.stream()
    	.forEach(flight -> {
    		String currency = flight.getCurrency();
    		
    		if(!currency.equals(STANDARD_CURRENCY)) {
    			double rate = currencyApi.getRate(currency, STANDARD_CURRENCY);
    			purchaseData.setPrice(flight.getPrice() * rate);
    		}
    	});
    	
    	Airline cheapest = foundFlights.stream().min(Comparator.comparing(Airline::getPrice)).get();
		purchaseData.setPrice(cheapest.getPrice());
		
        return purchaseData;
    }
}
