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


package com.citruspay.mobile.payment.oauth2;

import com.citruspay.mobile.payment.client.rest.Optional;

public class OAuth2ClientId {

	private final String id;
	private final Optional<String> secret;
	private final OAuth2GrantType grantType;
	
	public OAuth2ClientId(String id, Optional<String> secret, OAuth2GrantType grantType) {
		this.id = id;
		this.secret = secret;
		this.grantType = grantType;
	}
	
	public OAuth2ClientId(String id, OAuth2GrantType type) {
		this(id, Optional.<String>absent(), type);
	}
	
	public OAuth2ClientId(String id, String secret, OAuth2GrantType type) {
		this(id, Optional.of(secret), type);
	}

	public String getId() {
		return id;
	}

	public Optional<String> getSecret() {
		return secret;
	}

	public OAuth2GrantType getGrantType() {
		return grantType;
	}
}
