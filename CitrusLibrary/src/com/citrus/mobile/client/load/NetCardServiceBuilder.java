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

package com.citrus.mobile.client.load;

import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.oauth2.InMemoryTokenStore;
import com.citruspay.mobile.payment.oauth2.OAuth2ClientId;
import com.citruspay.mobile.payment.oauth2.OAuth2GrantType;
import com.citruspay.mobile.payment.oauth2.OAuth2Service;
import com.citruspay.mobile.payment.oauth2.OAuth2TokenStore;
import com.citruspay.mobile.payment.oauth2.SimpleOAuth2Service;

public class NetCardServiceBuilder {

	private OAuth2ClientId signedin;
	private OAuth2TokenStore tokens = new InMemoryTokenStore();
	private RESTClient restOauth, restPrepaid;
	
	public NetCardService build() {
		// oauth
	
		OAuth2Service in = new SimpleOAuth2Service(restOauth, signedin, tokens);
		
		// net card service
		return new NetCardService(restPrepaid, in);
	}


	public NetCardServiceBuilder tokens(OAuth2TokenStore store) {
		tokens = store;
		return this;
	}

	public NetCardServiceBuilder oauth(RESTClient restOauth) {
		this.restOauth = restOauth;
		return this;
	}

	public NetCardServiceBuilder prepaid(RESTClient restPrepaid) {
		this.restPrepaid = restPrepaid;
		return this;
	}

	public NetCardServiceBuilder signedin(String clientId,
			String clientSecret) {
		signedin = new OAuth2ClientId(clientId, clientSecret,
				OAuth2GrantType.password);
		return this;
	}
}
