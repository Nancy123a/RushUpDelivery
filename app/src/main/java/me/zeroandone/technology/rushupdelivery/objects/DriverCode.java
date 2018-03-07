package me.zeroandone.technology.rushupdelivery.objects;

import java.io.Serializable;

public class DriverCode implements Serializable{
    @com.google.gson.annotations.SerializedName("username")
    private String username = null;
    @com.google.gson.annotations.SerializedName("code")
    private String code = null;

    public DriverCode(String username, String code) {
        this.username = username;
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
