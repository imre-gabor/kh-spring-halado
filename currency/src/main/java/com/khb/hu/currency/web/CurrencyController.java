package com.khb.hu.currency.web;

import com.khb.hu.currency.api.CurrencyApi;
import com.khb.hu.currency.service.CurrencyService;
import com.khb.hu.currency.service.RateDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CurrencyController implements CurrencyApi {

    @Autowired
    private CurrencyService currencyService;
    
    @Override
    public double getRate(String from, String to) {
        return currencyService.getRate(new RateDescriptor(from, to));
    }
}
