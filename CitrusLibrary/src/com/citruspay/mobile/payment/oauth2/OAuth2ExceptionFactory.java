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

import com.citruspay.mobile.payment.client.rest.RESTException;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

public class OAuth2ExceptionFactory {

	public OAuth2Exception create(RESTException rx) {
		JSONObject content = rx.getContent();
		
		if (rx.getHttpStatusCode() == HttpStatus.SC_BAD_REQUEST) {
			if ("invalid_grant".equals(content.optString("error"))) {
				String type = null;
				if ("Bad credentials".equals(content.optString("error_description"))) {
					type = "BadCredentials";
				} else if ("User locked".equals(content.optString("error_description"))) {
					type = "UserLocked";
				} else {
					type = "Unknown";
				}
				try {
					content = new JSONObject()
					.put("errorType", type)
					.put("errorMessage", content.optString("error_description"));
				} catch (JSONException jx) {
					// ignore
				}
			}
		}
		
		return new OAuth2Exception(rx, content);
	}

}
