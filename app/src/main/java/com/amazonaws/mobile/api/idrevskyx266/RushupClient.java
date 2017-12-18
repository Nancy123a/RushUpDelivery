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
import me.zeroandone.technology.rushupdelivery.model.PushRequest;
import me.zeroandone.technology.rushupdelivery.model.TokenRequest;

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
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/token", method = "POST")
    void tokenPost(
            TokenRequest body);
    
}

