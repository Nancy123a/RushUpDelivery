package me.zeroandone.technology.rushupdelivery.objects;


import java.io.Serializable;

public class date_History implements Serializable{
    @com.google.gson.annotations.SerializedName("delivery_date")
    private Long datehistory = null;

    public date_History(Long datehistory) {
        this.datehistory = datehistory;
    }

    public void setDatehistory(Long datehistory) {
        this.datehistory = datehistory;
    }

    public Long getDatehistory() {
        return datehistory;
    }

    @Override
    public String toString() {
        return "{" +
                "datehistory=" + datehistory +
                '}';
    }
}
