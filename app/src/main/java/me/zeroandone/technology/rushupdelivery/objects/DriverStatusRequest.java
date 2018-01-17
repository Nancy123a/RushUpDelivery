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

public class DriverStatusRequest implements Serializable{
    @com.google.gson.annotations.SerializedName("status")
    private DriverStatus status = null;

    public DriverStatusRequest(DriverStatus status) {
        this.status = status;
    }

    /**
     * Gets status
     *
     * @return status
     **/
    public DriverStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of status.
     *
     * @param status the new value
     */
    public void setStatus(DriverStatus status) {
        this.status = status;
    }

}
