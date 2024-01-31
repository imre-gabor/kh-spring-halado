package com.khb.hu.springcourse.hr.service;

//@Retry(times = 5, waitTime=200) --> does not work
public interface IExternalSystemService {
    void callExternalService2();
}
