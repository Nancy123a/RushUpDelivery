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


public class DeliveryRequestDriverDriverLocation {
    @com.google.gson.annotations.SerializedName("latitude")
    private String latitude = null;
    @com.google.gson.annotations.SerializedName("longitude")
    private String longitude = null;

    /**
     * Gets latitude
     *
     * @return latitude
     **/
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of latitude.
     *
     * @param latitude the new value
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude
     *
     * @return longitude
     **/
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of longitude.
     *
     * @param longitude the new value
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
