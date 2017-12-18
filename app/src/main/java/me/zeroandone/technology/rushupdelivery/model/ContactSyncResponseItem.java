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
import java.util.List;

public class ContactSyncResponseItem implements Serializable {
    @com.google.gson.annotations.SerializedName("user_name")
    private String userName = null;
    @com.google.gson.annotations.SerializedName("phone_number")
    private String phoneNumber = null;
    @com.google.gson.annotations.SerializedName("locations")
    private List<ContactSyncResponseItemLocationsItem> locations = null;

    /**
     * Gets userName
     *
     * @return userName
     **/
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of userName.
     *
     * @param userName the new value
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets phoneNumber
     *
     * @return phoneNumber
     **/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of phoneNumber.
     *
     * @param phoneNumber the new value
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets locations
     *
     * @return locations
     **/
    public List<ContactSyncResponseItemLocationsItem> getLocations() {
        return locations;
    }

    /**
     * Sets the value of locations.
     *
     * @param locations the new value
     */
    public void setLocations(List<ContactSyncResponseItemLocationsItem> locations) {
        this.locations = locations;
    }

}
