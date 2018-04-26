package me.zeroandone.technology.rushupdelivery.objects;

/**
 * Created by nancy on 4/25/2018.
 */

public class UserRushup {
    @com.google.gson.annotations.SerializedName("phone")
    private String phone;

    public UserRushup(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
