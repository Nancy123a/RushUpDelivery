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

package com.amazonaws.mobile.api.idrevskyx266;


import me.zeroandone.technology.rushupdelivery.model.ContactSyncRequest;
import me.zeroandone.technology.rushupdelivery.model.ContactSyncResponse;
import me.zeroandone.technology.rushupdelivery.model.DeliveryStatusRequest;
import me.zeroandone.technology.rushupdelivery.model.DriverLocationRequest;
import me.zeroandone.technology.rushupdelivery.model.PushRequest;
import me.zeroandone.technology.rushupdelivery.model.TokenRequest;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;

@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://j9rgxxut6b.execute-api.eu-west-1.amazonaws.com/prod")
public interface RushupClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    com.amazonaws.mobileconnectors.apigateway.ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);

    /**
     *
     *
     * @param body
     * @return ContactSyncResponse
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/contact", method = "POST")
    ContactSyncResponse contactPost(
            ContactSyncRequest body);

    /**
     *
     *
     * @param body
     * @return DeliveryRequest
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/delivery", method = "POST")
    DeliveryRequest deliveryPost(
            DeliveryRequest body);

    /**
     *
     *
     * @param deliveryId
     * @return DeliveryRequest
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/delivery/{delivery_id}", method = "GET")
    DeliveryRequest deliveryDeliveryIdGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "delivery_id", location = "path")
                    String deliveryId);

    /**
     *
     *
     * @param deliveryId
     * @param body
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/delivery/{delivery_id}", method = "PUT")
    void deliveryDeliveryIdPut(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "delivery_id", location = "path")
                    String deliveryId,
            DeliveryStatusRequest body);

    /**
     *
     *
     * @param deliveryId
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/delivery/{delivery_id}/assign", method = "PUT")
    void deliveryDeliveryIdAssignPut(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "delivery_id", location = "path")
                    String deliveryId);

    /**
     *
     *
     * @param body
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/driver/location", method = "PUT")
    void driverLocationPut(
            DriverLocationRequest body);

    /**
     *
     *
     * @param body
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/driver/token", method = "POST")
    void driverTokenPost(
            TokenRequest body);

    /**
     *
     *
     * @param driverId
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/driver/{driver_id}", method = "GET")
    void driverDriverIdGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "driver_id", location = "path")
                    String driverId);


    /**
     *
     *
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/ping", method = "GET")
    void pingGet();

    /**
     *
     *
     * @param body
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/publish", method = "POST")
    void publishPost(
            PushRequest body);

    /**
     *
     *
     * @param body
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/rushie/token", method = "POST")
    void rushieTokenPost(
            TokenRequest body);

    /**
     *
     *
     * @param body
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/token", method = "POST")
    void tokenPost(
            TokenRequest body);

}