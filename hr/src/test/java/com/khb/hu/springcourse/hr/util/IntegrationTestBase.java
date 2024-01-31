package com.khb.hu.springcourse.hr.util;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class IntegrationTestBase {

    @Autowired
    TestDbUtil testDbUtil;

    @BeforeEach
    public void init(){
        testDbUtil.clearDb();
    }
}
