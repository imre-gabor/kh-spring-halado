package com.khb.hu.booking.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOlsiU0VBUkNIX0ZMSUdIVCJdLCJpc3MiOiJLSC1Ub2tlbi1BcHAiLCJleHAiOjE3MDY3ODMyNTB9.54ws7wHONNIPa-2dcPKG74rLGthav1vjDwsOEeD9iWA");
    }
}
