package me.zeroandone.technology.rushupdelivery.objects;

public class DriverBalance {
    DeliveryHistoryDriverResponse driver_balance;


    public DeliveryHistoryDriverResponse getDriver_balance() {
        return driver_balance;
    }

    public void setDriver_balance(DeliveryHistoryDriverResponse driver_balance) {
        this.driver_balance = driver_balance;
    }

    @Override
    public String toString() {
        return "{" +
                "driver_balance=" + driver_balance +
                '}';
    }
}
