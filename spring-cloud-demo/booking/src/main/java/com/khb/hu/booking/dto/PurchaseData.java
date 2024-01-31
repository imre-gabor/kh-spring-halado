package com.khb.hu.booking.dto;

public class PurchaseData {

    private boolean success;
    private double price;
    
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }    
}
