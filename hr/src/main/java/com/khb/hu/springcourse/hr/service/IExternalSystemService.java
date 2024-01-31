package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.aspect.Retry;

//@Retry(times = 5, waitTime=200) --> does not work
public interface IExternalSystemService {
    void callExternalService2();
}
