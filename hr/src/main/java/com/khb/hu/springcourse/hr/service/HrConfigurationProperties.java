package com.khb.hu.springcourse.hr.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hr")
public class HrConfigurationProperties {

    private PayRaise payRaise;

    public PayRaise getPayRaise() {
        return payRaise;
    }

    public void setPayRaise(PayRaise payRaise) {
        this.payRaise = payRaise;
    }

    public static class PayRaise{
        private int def;
        private int premium;
        private int limit;

        public int getDef() {
            return def;
        }

        public void setDef(int def) {
            this.def = def;
        }

        public int getPremium() {
            return premium;
        }

        public void setPremium(int premium) {
            this.premium = premium;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }
}
