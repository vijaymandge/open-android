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

import java.util.Date;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class OAuth2Token {

        private final JSONObject token;

        private OAuth2Token(JSONObject token) {
                this.token = token;
        }
        
        public static OAuth2Token create(JSONObject json) {
                // enhance object w/ expiry date
                JSONObject jtoken = json;
                if (!json.has("expiry")) {
                        // copy input
                        jtoken = new JSONObject();
                        for (Iterator<?> keys = json.keys(); keys.hasNext(); ) {
                                String key = (String) keys.next();
                                try {
                                        jtoken.put(key, json.get(key));
                                } catch (JSONException jx) {
                                        throw new RuntimeException(jx);
                                }
                        }
                        
                        // add expiry date
                        long expiry = new Date().getTime() / 1000l;
                        try {
                                expiry += json.getLong("expires_in");
                        } catch (JSONException jx) {
                                /* ignore => expires now ! */
                        }
                        try {
                                jtoken.put("expiry", expiry);
                        } catch (JSONException jx) {
                                throw new RuntimeException(jx);
                        }
                }
                
                // create token
                return new OAuth2Token(jtoken);
        }
        
        public String getAccessToken() {
                try {
                        return token.getString("access_token");
                } catch (JSONException jx) {
                        throw new RuntimeException("access token not found in JSON");
                }
        }
        
        public boolean hasRefreshToken() {
                return token.has("refresh_token");
        }

        public String getRefreshToken() {
                try {
                        return token.getString("refresh_token");
                } catch (JSONException jx) {
                        throw new RuntimeException("refresh token not found in JSON");
                }
        }
        
        public boolean hasExpired() {
                try {
                        return token.getLong("expiry") <= (new Date().getTime() / 1000l);
                } catch (JSONException jx) {
                        return true;
                }
        }
        
        public JSONObject asJSON() {
                return token;
        }
}