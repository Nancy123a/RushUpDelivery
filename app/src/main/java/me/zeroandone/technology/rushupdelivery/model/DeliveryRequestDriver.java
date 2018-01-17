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


import java.io.Serializable;

public class DeliveryRequestDriver implements Serializable{
    @com.google.gson.annotations.SerializedName("driver_location")
    private DriverLocationRequest driverLocation = null;
    @com.google.gson.annotations.SerializedName("identity_id")
    private String identityId = null;
    @com.google.gson.annotations.SerializedName("phone")
    private String phone = null;
    @com.google.gson.annotations.SerializedName("username")
    private String username = null;
    @com.google.gson.annotations.SerializedName("driver_status")
    private String driver_status = null;

    /**
     * Gets driverLocation
     *
     * @return driverLocation
     **/
    public DriverLocationRequest getDriverLocation() {
        return driverLocation;
    }

    /**
     * Sets the value of driverLocation.
     *
     * @param driverLocation the new value
     */
    public void setDriverLocation(DriverLocationRequest driverLocation) {
        this.driverLocation = driverLocation;
    }

    /**
     * Gets identityId
     *
     * @return identityId
     **/
    public String getIdentityId() {
        return identityId;
    }

    /**
     * Sets the value of identityId.
     *
     * @param identityId the new value
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    /**
     * Gets phone
     *
     * @return phone
     **/
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of phone.
     *
     * @param phone the new value
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets username
     *
     * @return username
     **/
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of username.
     *
     * @param username the new value
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getDriver_status() {
        return driver_status;
    }

    public void setDriver_status(String driver_status) {
        this.driver_status = driver_status;
    }


}
