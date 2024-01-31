package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.aspect.Retry;
import org.springframework.stereotype.Service;

import java.util.Random;

@Retry(times = 5, waitTime=200)
@Service
public class ExternalSystemService implements IExternalSystemService {

    Random random = new Random();

    @Override
    public void callExternalService2(){
        callExternalService();
        //a retry nem tud bekapcsolni, ha csak a callExternalService-en van annotáció,
        //mert ez egy lokális metódushívás, nincs közte az AOP proxy, ami meghívná az advice-t
    }



    public void callExternalService(){
        if(random.nextBoolean())
            throw new RuntimeException("calling external service failed");
    }
}
