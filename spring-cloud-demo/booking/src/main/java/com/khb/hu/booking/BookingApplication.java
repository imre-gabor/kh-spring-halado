package com.khb.hu.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.khb.hu.currency.api.CurrencyApi;
import com.khb.hu.flights.api.FlightsApi;

@EnableFeignClients(basePackageClasses = {CurrencyApi.class, FlightsApi.class})
@SpringBootApplication
public class BookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}

}
