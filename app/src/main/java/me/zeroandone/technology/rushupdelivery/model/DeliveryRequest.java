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

package me.zeroandone.technology.rushupdelivery.model;

import java.math.BigDecimal;

public class DeliveryRequest {
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
    @com.google.gson.annotations.SerializedName("id")
    private String id = null;
    @com.google.gson.annotations.SerializedName("to")
    private String to = null;
    @com.google.gson.annotations.SerializedName("delivery_status")
    private String deliveryStatus = null;
    @com.google.gson.annotations.SerializedName("dropoff_location")
    private DeliveryRequestPickupLocation dropoffLocation = null;

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
     * Gets driver
     *
     * @return driver
     **/
    public DeliveryRequestDriver getDriver() {
        return driver;
    }

    /**
     * Sets the value of driver.
     *
     * @param driver the new value
     */
    public void setDriver(DeliveryRequestDriver driver) {
        this.driver = driver;
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
     * Gets id
     *
     * @return id
     **/
    public String getId() {
        return id;
    }

    /**
     * Sets the value of id.
     *
     * @param id the new value
     */
    public void setId(String id) {
        this.id = id;
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
     * Gets deliveryStatus
     *
     * @return deliveryStatus
     **/
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * Sets the value of deliveryStatus.
     *
     * @param deliveryStatus the new value
     */
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

}
