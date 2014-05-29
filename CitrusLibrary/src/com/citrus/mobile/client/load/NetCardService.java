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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.client.rest.RESTException;
import com.citruspay.mobile.payment.oauth2.AuthorizationUtil;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;
import com.citruspay.mobile.payment.oauth2.OAuth2Service;

public class NetCardService {
	private final RESTClient rest;
	private final OAuth2Service signedin;

	public NetCardService(RESTClient rest, OAuth2Service signin) {
		this.rest = rest;
		this.signedin = signin;

	}
	
	public JSONObject getLoadValues(JSONObject loadValues) throws JSONException, ProtocolException,
			OAuth2Exception, PrepaidException {
		JSONObject signedOrder = null;
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("amount", loadValues.getString("amount"));
			params.put("currency", loadValues.getString("currency"));
			params.put("redirect", loadValues.getString("redirect"));
		} catch (JSONException e) {
			throw new JSONException("parameter values not passed correctly");
		}
		
		
		try {
			signedOrder = rest.post(URI.create("load/extended/"),
					AuthorizationUtil.asHeader(signedin.getAuthorization()),
					params);
		} catch (RESTException e) {
			handleRESTException(e);
		}
		
		return signedOrder;
	}

    public Amount getBalance() throws ProtocolException, PrepaidException,
            OAuth2Exception, IllegalArgumentException {
        JSONObject mycard = null;
        try {
            mycard = rest.get(URI.create(""),
                    AuthorizationUtil.asHeader(signedin.getAuthorization()));

        } catch (RESTException re) {
            handleRESTException(re);
        }
        try {
            return new Amount(
                    Float.valueOf(mycard.getString("value")),
                    mycard.getString("currency"));
        } catch (NumberFormatException nfx) {
            throw new RuntimeException(nfx);
        } catch (JSONException jx) {
            throw new ProtocolException(jx);
        }

    }
	
	private JSONObject handleRESTException(RESTException rx)
			throws ProtocolException, PrepaidException {
		if (rx.getHttpStatusCode() == HttpStatus.SC_BAD_REQUEST) {
			throw new PrepaidException(rx, rx.getContent());
		} else {
			throw new ProtocolException(rx);
		}
	}
}
