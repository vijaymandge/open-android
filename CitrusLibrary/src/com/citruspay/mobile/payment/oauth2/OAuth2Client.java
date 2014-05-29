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

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.client.rest.RESTException;

import org.apache.http.Header;
import org.json.JSONObject;

public class OAuth2Client {
	private final OAuth2ExceptionFactory factory = new OAuth2ExceptionFactory();
	
	private final OAuth2ClientId clientId;
	private final RESTClient rest;

	public OAuth2Client(OAuth2ClientId clientId, RESTClient rest) {
		this.clientId = clientId;
		this.rest = rest;
	}

	public OAuth2Token getToken() throws ProtocolException, OAuth2Exception {
		return getToken(Collections.<String, String> emptyMap());
	}

	public OAuth2Token getToken(Map<String, String> params)
			throws ProtocolException, OAuth2Exception {
		JSONObject json = null;
		try {
			json = rest.post(
					URI.create("token"),
					Collections.<Header> emptyList(),
					buildRequest(params));
		}  catch (RESTException rx) {
			throw factory.create(rx);
		}
		return OAuth2Token.create(json);
	}

	public OAuth2ClientId getClientId() {
		return clientId;
	}

	protected Map<String, String> buildRequest() {
		Map<String, String> request = new HashMap<String, String>();
		request.put("client_id", clientId.getId());
		request.put("grant_type", clientId.getGrantType().toString());
		if (clientId.getSecret().isPresent()) {
			request.put("client_secret", clientId.getSecret().get());
		}
		return request;
	}

	protected Map<String, String> buildRequest(Map<String, String> params) {
		Map<String, String> request = new HashMap<String, String>();
		request.put("client_id", clientId.getId());
		request.put("grant_type", clientId.getGrantType().toString());
		if (clientId.getSecret().isPresent()) {
			request.put("client_secret", clientId.getSecret().get());
		}
		request.putAll(params);
		return request;
	}
}
