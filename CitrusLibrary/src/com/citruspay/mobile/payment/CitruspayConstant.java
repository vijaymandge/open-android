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


package com.citruspay.mobile.payment;

public class CitruspayConstant {
    public static final String API_BASE = "/api/v1/txn";
	public static final String VERSION = "1.0";
	public static String merchantKey;
	public static final String CHARSET = "UTF-8";
	public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	public static final String REQ_SIGNATURE="signature";
	public static final String UNPERSABLE_RESPONE="{"+"\"resp_msg\"" +":"+"\"Unprocessable Entity: Invalid response received\""+","+"\"resp_code\""+":"+"\"422\""+"}";
}
