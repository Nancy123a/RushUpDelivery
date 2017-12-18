package me.zeroandone.technology.rushup.objects;


public class BalanceObject {
    String PickupDelivery,dollars;

    public BalanceObject(String pickupDelivery, String dollars) {
        PickupDelivery = pickupDelivery;
        this.dollars = dollars;
    }

    public String getPickupDelivery() {
        return PickupDelivery;
    }

    public void setPickupDelivery(String pickupDelivery) {
        PickupDelivery = pickupDelivery;
    }

    public String getDollars() {
        return dollars;
    }

    public void setDollars(String dollars) {
        this.dollars = dollars;
    }

    @Override
    public String toString() {
        return "{" +
                "PickupDelivery='" + PickupDelivery + '\'' +
                ", dollars='" + dollars + '\'' +
                '}';
    }
}
