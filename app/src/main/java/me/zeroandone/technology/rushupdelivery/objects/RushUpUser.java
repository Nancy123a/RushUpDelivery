package me.zeroandone.technology.rushupdelivery.objects;


import java.io.Serializable;

public class RushUpUser implements Serializable {
    @com.google.gson.annotations.SerializedName("username")
    private String username;
    @com.google.gson.annotations.SerializedName("phone")
    private String phone;
    @com.google.gson.annotations.SerializedName("token")
    private String token;
    @com.google.gson.annotations.SerializedName("endpoint_arn")
    private String endpoint_arn;
    @com.google.gson.annotations.SerializedName("identity_id")
    private String identity_id;

    public RushUpUser(String username, String phone, String token, String endpoint_arn, String identity_id) {
        this.username = username;
        this.phone = phone;
        this.token = token;
        this.endpoint_arn = endpoint_arn;
        this.identity_id = identity_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEndpoint_arn() {
        return endpoint_arn;
    }

    public void setEndpoint_arn(String endpoint_arn) {
        this.endpoint_arn = endpoint_arn;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", endpoint_arn='" + endpoint_arn + '\'' +
                ", identity_id='" + identity_id + '\'' +
                '}';
    }
}
