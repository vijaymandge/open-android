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

    public static final String ACCESS_KEY = "WEO99TS6PQ1ZHERXP0NZ";

    public static final String SignUpClientId = "citrus-mobile-subscription";

    public static final String SignUpClientSecret = "2e6d37aa23a868e043705ba539da999a";

    public static final String SignInpClientId = "citrus-mobile-app-v1";

    public static final String SignInClientSecret = "0e49deace77ab85a434324c3c13ae9f2";
	
	public final static boolean prodEnv = true;

    public final static String PAY_TYPE = "type";

    public static final String GUEST_FLOW = "guestFlow";

    public static final String MEMBER_FLOW = "memberFlow";

    public static final String USER_EXISTS = "com.citruspay.directory.exception.CitrusUserAlreadyExistsException";

	public static final boolean isSignedIn(Activity activity) {
		MobileClient mobileClient = new MobileClient(activity);	
		return mobileClient.isSignedIn(SignInpClientId);
	}
}