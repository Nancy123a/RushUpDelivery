/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package me.zeroandone.technology.rushupdelivery.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import me.zeroandone.technology.rushupdelivery.model.DeliveryRequestDriver;
import me.zeroandone.technology.rushupdelivery.model.DeliveryRequestPickupLocation;

public class DeliveryRequest implements Serializable{
    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("delivery_date")
    private BigDecimal deliveryDate = null;
    @com.google.gson.annotations.SerializedName("driver")
    private DeliveryRequestDriver driver = null;
    @com.google.gson.annotations.SerializedName("pickup_location")
    private DeliveryRequestPickupLocation pickupLocation = null;
    @com.google.gson.annotations.SerializedName("dropoff_name")
    private String dropoffName = null;
    @com.google.gson.annotations.SerializedName("pickup_name")
    private String pickupName = null;
    @com.google.gson.annotations.SerializedName("from")
    private String from = null;
    @com.google.gson.annotations.SerializedName("to")
    private String to = null;
    @com.google.gson.annotations.SerializedName("dropoff_location")
    private DeliveryRequestPickupLocation dropoffLocation = null;
    @com.google.gson.annotations.SerializedName("delivery_status")
    private DeliveryStatus delivery_status = null;
    @com.google.gson.annotations.SerializedName("page")
    private  int page;
    @com.google.gson.annotations.SerializedName("isrushiepickup")
    boolean isRushie_PickUp;
    @com.google.gson.annotations.SerializedName("isrushiedropoff")
    boolean isRushie_DropOff;
    @com.google.gson.annotations.SerializedName("identity_id")
    private String identity_id;
    @com.google.gson.annotations.SerializedName("to_code")
    private String  to_code;
    @com.google.gson.annotations.SerializedName("from_code")
    private String from_code;

    public DeliveryRequest() {
    }

    public DeliveryRequest(String id, BigDecimal deliveryDate, DeliveryRequestDriver driver, DeliveryRequestPickupLocation pickupLocation, String dropoffName, String pickupName, String from, String to, DeliveryRequestPickupLocation dropoffLocation, DeliveryStatus delivery_status, int page, boolean isRushie_PickUp, boolean isRushie_DropOff, String pickUpPhonenumber,String from_code,String to_code) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.driver = driver;
        this.pickupLocation = pickupLocation;
        this.dropoffName = dropoffName;
        this.pickupName = pickupName;
        this.from = from;
        this.to = to;
        this.dropoffLocation = dropoffLocation;
        this.delivery_status = delivery_status;
        this.page = page;
        this.isRushie_PickUp = isRushie_PickUp;
        this.isRushie_DropOff = isRushie_DropOff;
        identity_id = pickUpPhonenumber;
        this.from_code=from_code;
        this.to_code=to_code;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets deliveryDate
     *
     * @return deliveryDate
     **/
    public BigDecimal getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the value of deliveryDate.
     *
     * @param deliveryDate the new value
     */
    public void setDeliveryDate(BigDecimal deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Gets pickupLocation
     *
     * @return pickupLocation
     **/
    public DeliveryRequestPickupLocation getPickupLocation() {
        return pickupLocation;
    }

    /**
     * Sets the value of pickupLocation.
     *
     * @param pickupLocation the new value
     */
    public void setPickupLocation(DeliveryRequestPickupLocation pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * Gets dropoffName
     *
     * @return dropoffName
     **/
    public String getDropoffName() {
        return dropoffName;
    }

    /**
     * Sets the value of dropoffName.
     *
     * @param dropoffName the new value
     */
    public void setDropoffName(String dropoffName) {
        this.dropoffName = dropoffName;
    }

    /**
     * Gets pickupName
     *
     * @return pickupName
     **/
    public String getPickupName() {
        return pickupName;
    }

    /**
     * Sets the value of pickupName.
     *
     * @param pickupName the new value
     */
    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    /**
     * Gets from
     *
     * @return from
     **/
    public String getFrom() {
        return from;
    }

    /**
     * Sets the value of from.
     *
     * @param from the new value
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets to
     *
     * @return to
     **/
    public String getTo() {
        return to;
    }

    /**
     * Sets the value of to.
     *
     * @param to the new value
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets dropoffLocation
     *
     * @return dropoffLocation
     **/
    public DeliveryRequestPickupLocation getDropoffLocation() {
        return dropoffLocation;
    }

    /**
     * Sets the value of dropoffLocation.
     *
     * @param dropoffLocation the new value
     */
    public void setDropoffLocation(DeliveryRequestPickupLocation dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public DeliveryStatus getStatus() {
        return delivery_status;
    }

    public void setStatus(DeliveryStatus delivery_status) {
        this.delivery_status = delivery_status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public DeliveryStatus getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(DeliveryStatus delivery_status) {
        this.delivery_status = delivery_status;
    }

    public boolean isRushie_PickUp() {
        return isRushie_PickUp;
    }

    public void setRushie_PickUp(boolean rushie_PickUp) {
        isRushie_PickUp = rushie_PickUp;
    }

    public boolean isRushie_DropOff() {
        return isRushie_DropOff;
    }

    public void setRushie_DropOff(boolean rushie_DropOff) {
        isRushie_DropOff = rushie_DropOff;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public DeliveryRequestDriver getDriver() {
        return driver;
    }

    public void setDriver(DeliveryRequestDriver driver) {
        this.driver = driver;
    }

    public String getTo_code() {
        return to_code;
    }

    public void setTo_code(String to_code) {
        this.to_code = to_code;
    }

    public String getFrom_code() {
        return from_code;
    }

    public void setFrom_code(String from_code) {
        this.from_code = from_code;
    }

    @Override
    public String toString() {
        return "DeliveryRequest{" +
                "id='" + id + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", driver=" + driver +
                ", pickupLocation=" + pickupLocation +
                ", dropoffName='" + dropoffName + '\'' +
                ", pickupName='" + pickupName + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", dropoffLocation=" + dropoffLocation +
                ", delivery_status=" + delivery_status +
                ", page=" + page +
                ", isRushie_PickUp=" + isRushie_PickUp +
                ", isRushie_DropOff=" + isRushie_DropOff +
                ", identity_id='" + identity_id + '\'' +
                ", to_code='" + to_code + '\'' +
                ", from_code='" + from_code + '\'' +
                '}';
    }
}
