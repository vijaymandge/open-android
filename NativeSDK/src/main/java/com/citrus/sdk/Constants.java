/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.citrus.sdk;

import android.app.Activity;

import com.citruspay.mobile.client.MobileClient;

public class Constants {


    /*Do not change the following URLs*/
    public static final String CITRUS_STRUCT_URL = "https://sandboxadmin.citruspay.com/service/moto/authorize/struct/payment";

    public static final String GUEST_PAY_URL = "https://sandboxadmin.citruspay.com/api/v2/txn/create";

    public static final String CITRUS_OAUTH_URL = "https://sandboxadmin.citruspay.com/";


   /*Folowing are the citrus apk merchant details Merchant needs to replace it with their details
   * Please contact support team at Citrus, if you need any further assistance.
   * */

    public static final String SIGNATURE_URL = "http://sandbox.citruspay.com/namo/sign.php";

    public static final String REDIRECT_URL = "https://admin.citruspay.com/requestData";

    public static final String VANITY_URL = "nativeSDK";

    public static final String ACCESS_KEY = "F2VZD1HBS2VVXJPMWO77";

    public static final String SUBSCRIPTION_ID = "citrus-native-mobile-subscription";

    public static final String SUBSCRIPTION_SECRET = "3e2288d3a1a3f59ef6f93373884d2ca1";

    public static final String SIGNIN_ID = "citrus-native-mobile-app-v1";

    public static final String SIGNIN_SECRET = "83df0e4db17fa7b206f4c36d3f19d6c1";




    /*Other merchant non-specific constants*/

    public static final String PAY_TYPE = "type";

    public static final String GUEST_FLOW = "guestFlow";

    public static final String MEMBER_FLOW = "memberFlow";

    public static final String USER_EXISTS = "com.citruspay.directory.exception.CitrusUserAlreadyExistsException";

    public static final String SMS_RECEIVED = "com.citrus.sdk.sms.received";


}