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
	public static final String CITRUS_SERVER_URL = "https://stgadmin.citruspay.com/service/moto/authorize/struct/shardul";
	//public static final String CITRUS_SERVER_URL = "https://oops.citruspay.com/service/moto/authorize/struct/shardul";
	//public static final String CITRUS_SERVER_URL = "https://admin.citruspay.com/api/v2/txn/create";
	public static final String REDIRECT_URL = "http://103.13.97.20/citrus/index.php";

    /*public static final String ACCESS_KEY = "WEO99TS6PQ1ZHERXP0NZ";

    public static final String SECRET_KEY = "9c350b57ebb6562adaf389972c717a33b893d4f8";

    public static final String SUBSCRIPTION_ID = "citrus-mobile-subscription";

    public static final String SUBSCRIPTION_SECRET = "2e6d37aa23a868e043705ba539da999a";

    public static final String SIGNIN_ID = "citrus-mobile-app-v1";

    public static final String SIGNIN_SECRET = "0e49deace77ab85a434324c3c13ae9f2";*/

    //Folowing are the citrus apk merchant details

    public static final String ACCESS_KEY = "14KZ1O3AGP8SUKPK8989";

    public static final String SECRET_KEY = "662bd8628cf79a2f96b79c5d29418ecb4031bead";

    public static final String SUBSCRIPTION_ID = "citrus-native-mobile-subscription";

    public static final String SUBSCRIPTION_SECRET = "3e2288d3a1a3f59ef6f93373884d2ca1";

    public static final String SIGNIN_ID = "citrus-native-mobile-app-v1";

    public static final String SIGNIN_SECRET = "83df0e4db17fa7b206f4c36d3f19d6c1";
	
	public final static boolean prodEnv = true;

    public final static String PAY_TYPE = "type";

    public static final String GUEST_FLOW = "guestFlow";

    public static final String MEMBER_FLOW = "memberFlow";

    public static final String USER_EXISTS = "com.citruspay.directory.exception.CitrusUserAlreadyExistsException";

    public static final String GUEST_PAY_URL = "https://stgadmin.citruspay.com/api/v2/txn/create";

	public static final boolean isSignedIn(Activity activity) {
		MobileClient mobileClient = new MobileClient(activity);	
		return mobileClient.isSignedIn(SIGNIN_ID);
	}
}