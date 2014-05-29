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

package com.citruspay.mobile.client;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.client.subscription.SubscriptionServiceBuilder;
import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.oauth2.AndroidTokenStore;
import com.citruspay.mobile.payment.oauth2.OAuth2TokenStore;

public class MobileSubscriptionClient implements MobileSubscriptionProvider {
	private boolean mSignInFlag, mClearData;
	
	private URI oauth = URI.create(MobileKitConstants.SERVER_URL + "/identity/");

	private URI subscription = URI.create(MobileKitConstants.SERVER_URL
			+ "/profile/");

	private OAuth2TokenStore token;

	private HttpClient http = new DefaultHttpClient();

	private RESTClient restOauth = new RESTClient(oauth, http);

	private RESTClient restSubscription = new RESTClient(subscription, http);

	public MobileSubscriptionClient() {

	}

	public MobileSubscriptionClient(Activity activity) {
		token = new AndroidTokenStore(activity);
		mSignInFlag = false;
		mClearData = false;
	}
	
	public SubscriptionService getSubscriptionService(String SignUpClientId,
			String SignUpClientSecret, String SignInpClientId,
			String SignInClientSecret) {
		if (mSignInFlag) {
			((AndroidTokenStore) token).clearToken(SignUpClientId, mClearData);
		}
	
		return new SubscriptionServiceBuilder().subscription(restSubscription)
				.oauth(restOauth).signup(SignUpClientId, SignUpClientSecret)
				.signedin(SignInpClientId, SignInClientSecret).tokens(token)
				.build();
	}

}