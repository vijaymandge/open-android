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
import java.security.KeyStore;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;

import com.citrus.mobile.client.load.NetCardService;
import com.citrus.mobile.client.load.NetCardServiceBuilder;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.client.subscription.SubscriptionServiceBuilder;
import com.citruspay.mobile.payment.client.rest.MySSLSocketFactory;
import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.oauth2.AndroidTokenStore;
import com.citruspay.mobile.payment.oauth2.OAuth2TokenStore;

public class MobileClient implements MobileServiceProvider {
	private Activity mActivity;
	
	private URI oauth = URI
			.create(MobileKitConstants.SERVER_URL + "oauth/token");

	private URI subscription = URI
			.create(MobileKitConstants.SERVER_URL + "service/v2/");
	
	private URI prepaid = URI
			.create(MobileKitConstants.SERVER_URL + "service/v2/mycard/");
	
	private OAuth2TokenStore token; 	

	private HttpClient http =  new DefaultHttpClient();

	private RESTClient restOauth = new RESTClient(oauth, http);

	private RESTClient restSubscription = new RESTClient(subscription, http);
	
	private RESTClient restPrepaid = new RESTClient(prepaid, http);
	
	public MobileClient(Activity activity) {
		this.mActivity = activity;
		token = new AndroidTokenStore(this.mActivity);
	}
	
	public SubscriptionService getSubscriptionService(String SignUpClientId,
			String SignUpClientSecret, String SignInpClientId,
			String SignInClientSecret) {
		
		return new SubscriptionServiceBuilder().subscription(restSubscription)
				.oauth(restOauth).signup(SignUpClientId, SignUpClientSecret)
				.signedin(SignInpClientId, SignInClientSecret).tokens(token).http(http)
				.build();
	}

	@Override
	public NetCardService getNetCardService(String SignInClientId,
			String SignInClientSecret) {
		return new NetCardServiceBuilder().prepaid(restPrepaid)
				.oauth(restOauth).signedin(SignInClientId, SignInClientSecret)
				.tokens(token).build();
	}
	
	public boolean isSignedIn(String SignInClientId) {
		return token.contains(SignInClientId);
	}

}
