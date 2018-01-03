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


public class PushRequest {
    @com.google.gson.annotations.SerializedName("phone")
    private String phone = null;
    @com.google.gson.annotations.SerializedName("message")
    private String message = null;
    @com.google.gson.annotations.SerializedName("type")
    private String type = null;

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
     * Gets message
     *
     * @return message
     **/
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of message.
     *
     * @param message the new value
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets type
     *
     * @return type
     **/
    public String getType() {
        return type;
    }

    /**
     * Sets the value of type.
     *
     * @param type the new value
     */
    public void setType(String type) {
        this.type = type;
    }

}
