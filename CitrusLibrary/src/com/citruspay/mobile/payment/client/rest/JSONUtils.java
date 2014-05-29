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

package com.citruspay.mobile.payment.client.rest;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public final class JSONUtils {

        private JSONUtils() {}
        
        public static Date getDate(JSONObject json, String name) throws JSONException {
                return new Date(json.getLong(name));
        }
        
        public static Date optDate(JSONObject json, String name) {
                try {
                        return getDate(json, name);
                } catch (JSONException jx) {
                        return null;
                }
        }
        
        public static Date getDate(long millisec) throws JSONException {
                return new Date(millisec);
        }
}