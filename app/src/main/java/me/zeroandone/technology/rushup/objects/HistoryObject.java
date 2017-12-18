package me.zeroandone.technology.rushup.objects;


public class HistoryObject {
    String pickup_point,date,time,price,dropoff_point;

    public HistoryObject(String pickup_point, String date, String time, String price, String dropoff_point) {
        this.pickup_point = pickup_point;
        this.date = date;
        this.time = time;
        this.price = price;
        this.dropoff_point = dropoff_point;
    }

    public String getPickup_point() {
        return pickup_point;
    }

    public void setPickup_point(String pickup_point) {
        this.pickup_point = pickup_point;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDropoff_point() {
        return dropoff_point;
    }

    public void setDropoff_point(String dropoff_point) {
        this.dropoff_point = dropoff_point;
    }

    @Override
    public String toString() {
        return "{" +
                "pickup_point='" + pickup_point + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", price='" + price + '\'' +
                ", dropoff_point='" + dropoff_point + '\'' +
                '}';
    }
}
