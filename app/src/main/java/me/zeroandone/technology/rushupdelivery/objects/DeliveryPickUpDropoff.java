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

public class DeliveryPickUpDropoff implements Serializable{
    @com.google.gson.annotations.SerializedName("code")
    private String code = null;
    @com.google.gson.annotations.SerializedName("delivery_status")
    private String deliveryStatus = null;


    public DeliveryPickUpDropoff(String code, String deliveryStatus) {
        this.code = code;
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * Gets code
     *
     * @return code
     **/
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of code.
     *
     * @param code the new value
     */
    public void setCode(String code) {
        this.code = code;
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

}
