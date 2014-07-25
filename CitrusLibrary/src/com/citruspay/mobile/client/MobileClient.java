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
import com.citruspay.mobile.client.subscription.OpenService;
import com.citruspay.mobile.client.subscription.OpenServiceBuilder;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.client.subscription.SubscriptionServiceBuilder;
import com.citruspay.mobile.payment.client.rest.MySSLSocketFactory;
import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.oauth2.AndroidTokenStore;
import com.citruspay.mobile.payment.oauth2.OAuth2TokenStore;

public class MobileClient implements MobileServiceProvider {
	private Activity mActivity;

    private String server_url;
	
	private URI oauth;

	private URI subscription;

    private URI open_url;
	
	private OAuth2TokenStore token; 	

	private HttpClient http;

	private RESTClient restOauth;

	private RESTClient restSubscription;

    private RESTClient restOpen;
	
	public MobileClient(Activity activity, String oauth_url) {
		this.mActivity = activity;

        server_url = oauth_url;

        initVariables();
	}
	
	public SubscriptionService getSubscriptionService(String SignUpClientId,
			String SignUpClientSecret, String SignInpClientId,
			String SignInClientSecret) {
		
		return new SubscriptionServiceBuilder().subscription(restSubscription)
				.oauth(restOauth).signup(SignUpClientId, SignUpClientSecret)
				.signedin(SignInpClientId, SignInClientSecret).tokens(token).http(http)
				.build();
	}


    public OpenService getOpenService(String SignUpClientId, String SignUpClientSecret) {
        return new OpenServiceBuilder()
                   .tokens(token)
                   .oauth(restOauth)
                   .signup(SignUpClientId, SignUpClientSecret)
                   .openserv(restOpen)
                   .build();
    }

	public boolean isSignedIn(String SignInClientId) {
		return token.contains(SignInClientId);
	}

    private void initVariables() {
        oauth = URI.create(server_url + "oauth/token");

        subscription = URI.create(server_url + "service/v2/");

        open_url = URI.create(server_url + "service/v1/");

        http = new DefaultHttpClient();

        token = new AndroidTokenStore(this.mActivity);

        restOauth = new RESTClient(oauth, http);

        restSubscription = new RESTClient(subscription, http);

        restOpen = new RESTClient(open_url, http);
    }

}
