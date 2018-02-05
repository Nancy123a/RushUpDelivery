package me.zeroandone.technology.rushupdelivery.objects;

import java.util.List;


public class DriverDeliveryHistory {

    DeliveryHistoryDriverResponse driver_history;

    public DeliveryHistoryDriverResponse getDriver_history() {
        return driver_history;
    }

    public void setDriver_history(DeliveryHistoryDriverResponse driver_history) {
        this.driver_history = driver_history;
    }

    @Override
    public String toString() {
        return "{" +
                "driver_history=" + driver_history +
                '}';
    }
}
