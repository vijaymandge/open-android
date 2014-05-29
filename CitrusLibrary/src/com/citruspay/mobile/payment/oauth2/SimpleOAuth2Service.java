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

import java.util.HashMap;
import java.util.Map;

import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.client.rest.RESTClient;

import org.json.JSONException;
import org.json.JSONObject;

public class SimpleOAuth2Service implements OAuth2Service {

        private final RESTClient oauth;
        private final OAuth2ClientId client;
        private final OAuth2TokenStore tokens;
        
        public SimpleOAuth2Service(
                        RESTClient oauth, 
                        OAuth2ClientId client,
                        OAuth2TokenStore tokens) {
                this.oauth = oauth;
                this.client = client;
                this.tokens = tokens;
        }

        public OAuth2ClientId getClient() {
                return client;
        }

        @Override
        public OAuth2Token signin(String username, Object credential) throws ProtocolException, OAuth2Exception {
                // get token
                OAuth2Token token = new OAuth2Client(client, oauth)
                .getToken(AuthorizationUtil.asParams(username, credential));
                
                // store it
                tokens.store(client.getId(), token);
                
                // return
                return token;
        }

        @Override
        public OAuth2Token getAuthorization() throws ProtocolException, OAuth2Exception {
                // get token from store
                if (tokens.contains(client.getId())) {
                        OAuth2Token token = tokens.load(client.getId());
                        if (token.hasExpired()) {
                                token = refresh(token);
                        }
                        return token;
                }
                
                // check implicit client
                if (client.getGrantType() != OAuth2GrantType.implicit) {
                        try {
                                throw new OAuth2Exception(new JSONObject()
                                .put("errorType", "GrantTypeMismatch")
                                .put("errorMessage", "implicit grant type expected"));
                        } catch (JSONException jx) {
                                throw new RuntimeException(jx);
                        }
                }
                
                // get implicit token
                OAuth2Token token = new OAuth2Client(client, oauth)
                .getToken();
                
                // store it
                tokens.store(client.getId(), token);
                
                // return
                return token;
        }
        
        protected OAuth2Token refresh(OAuth2Token token) throws ProtocolException, OAuth2Exception {
                // no refresh guard
                if (!token.hasRefreshToken()) {
                        return token;
                }
                
                // refresh params
                Map<String, String> refresh = new HashMap<String, String>();
                refresh.put("grant_type", "refresh_token");
                refresh.put("refresh_token", token.getRefreshToken());
                
                // refresh token
                OAuth2Token refreshed = new OAuth2Client(client, oauth).getToken(refresh);
                
                // store it
                tokens.store(client.getId(), refreshed);
                
                // return
                return refreshed;
        }

		@Override
		public void storeCookie(String key, String cookie) {
					
		}
                
}