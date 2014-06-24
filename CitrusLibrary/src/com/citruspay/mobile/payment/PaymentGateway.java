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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.citruspay.mobile.payment.client.rest.MySSLSocketFactory;

public class PaymentGateway {
	private String serverUrl;
	private String accessKey;
	private JSONObject txnObject;
	public PaymentGateway(String serverUrl, String accessKey, JSONObject txnobject) {
		this.serverUrl = serverUrl;
		this.accessKey = accessKey;
		this.txnObject = txnobject;
	}
	
	public JSONObject postPayment() {
		JSONObject result = null;
		HttpResponse response = null;
		
		try {
			URL url = new URL(serverUrl);
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url.toURI());
			
			/*These headers are compulsory*/
			
			httpPost.setHeader("access_key", accessKey);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept-Encoding", "application/json");
			httpPost.setHeader("Accept-Language", "en-US");
			
			httpPost.setEntity(new StringEntity(txnObject.toString()));			
			response = httpClient.execute(httpPost);
			StringBuffer res = new StringBuffer();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    String inputLine;
				   while ((inputLine = in.readLine()) != null) {
				    res.append(inputLine);
				   }

				   in.close();
			
			result = new JSONObject(res.toString());
		} catch (Exception e) {
			throw new RuntimeException(String.valueOf(response.getStatusLine().getStatusCode()));
		}
		
		return result;	
	}


}
