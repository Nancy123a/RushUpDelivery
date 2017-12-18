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

package me.zeroandone.technology.rushup.model;


import java.io.Serializable;

public class ContactSyncResponseItemLocationsItem implements Serializable {
    @com.google.gson.annotations.SerializedName("value")
    private String value = null;
    @com.google.gson.annotations.SerializedName("key")
    private String key = null;

    /**
     * Gets value
     *
     * @return value
     **/
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of value.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets key
     *
     * @return key
     **/
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of key.
     *
     * @param key the new value
     */
    public void setKey(String key) {
        this.key = key;
    }

}
