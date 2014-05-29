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

package com.citruspay.mobile.client.subscription;

import org.apache.http.client.HttpClient;

import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.oauth2.OAuth2ClientId;
import com.citruspay.mobile.payment.oauth2.OAuth2GrantType;
import com.citruspay.mobile.payment.oauth2.OAuth2Service;
import com.citruspay.mobile.payment.oauth2.OAuth2TokenStore;
import com.citruspay.mobile.payment.oauth2.SimpleOAuth2Service;

public class SubscriptionServiceBuilder {

	private OAuth2ClientId signup, signedin;
	private OAuth2TokenStore tokens ;
	private PasswordGenerator passwords = new PseudoRandomPasswordGenerator();
	private RESTClient restOauth, restSubscription;
	private HttpClient http;


	public SubscriptionService build() {
		// oauth services

		OAuth2Service up = new SimpleOAuth2Service(restOauth, signup, tokens);
		OAuth2Service in = new SimpleOAuth2Service(restOauth, signedin, tokens);


		// build new service
		return new SubscriptionService(up, in, restSubscription, http, passwords);
	}

	public SubscriptionServiceBuilder passwords(PasswordGenerator generator) {
		passwords = generator;
		return this;
	}

	public SubscriptionServiceBuilder tokens(OAuth2TokenStore store) {
		tokens = store;
		return this;
	}
  
	public SubscriptionServiceBuilder oauth(RESTClient restOauth ) {
		this.restOauth = restOauth;
		return this;
	}

	public SubscriptionServiceBuilder subscription(RESTClient restSubscription) {
		this.restSubscription = restSubscription;
		return this;
	}

	public SubscriptionServiceBuilder signup(String clientId,
			String clientSecret) {
		signup = new OAuth2ClientId(clientId, clientSecret,
				OAuth2GrantType.implicit);
		return this;
	}


	public SubscriptionServiceBuilder http(HttpClient http) {
	this.http = http;
		return this;
	}

	public SubscriptionServiceBuilder signedin(String clientId,
			String clientSecret) {
		signedin = new OAuth2ClientId(clientId, clientSecret,
				OAuth2GrantType.password);
		return this;
	}

}