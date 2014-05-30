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

package com.citrus.sdk.webops;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.citrus.sdk.interfaces.Messenger;
import com.citruspay.util.HMACSignature;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class GuestPay extends AsyncTask <Void, Void, Void>{
	private final static String GUEST_POST_URL = "https://admin.citruspay.com/api/v2/txn/create";
	private final static String ACCESS_KEY = "08GXHDIMWZS2IEUG3M9U";
	private final static String SECRET_KEY = "1c770f0f925597fa640c4a3a00a5fa050a6890bd";
	
	private ProgressDialog dialog;
	private Activity activity;
	private Messenger listener;
	private JSONObject txnObject;
	private String redirectUrl, txnId;
	
	
	public GuestPay(Activity activity, JSONObject txnObject, Messenger listener, String txnId) {
		this.listener = listener;
		this.activity = activity;
		this.txnObject = txnObject;
		this.txnId = txnId;
	}
	
	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(activity, "Please Wait", "Redirecting to Citrus", true, true);
		redirectUrl = null;
	}
	
	@Override
	protected Void doInBackground(Void... params) { 
		URL url;
		HttpPost httpPost;
		HttpClient httpClient;
		try {
			url = new URL(GUEST_POST_URL);
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url.toURI());
			String data = "merchantAccessKey=" + ACCESS_KEY + "&transactionId=" + txnId + "&amount=1.0";
			String signature = HMACSignature.generateHMAC(data, SECRET_KEY);
			httpPost.setHeader("access_key", ACCESS_KEY);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept-Encoding", "application/json");
			httpPost.setHeader("Accept-Language", "en-US");
			httpPost.setHeader("signature", signature);
			
			httpPost.setEntity(new StringEntity(txnObject.toString()));			
			HttpResponse response = httpClient.execute(httpPost);
			StringBuffer res = new StringBuffer();

			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    String inputLine;
				   while ((inputLine = in.readLine()) != null) {
				    res.append(inputLine);
				   }

				   in.close();

		    String[] splitString = res.toString().split("<redirectUrl>");
		    splitString = splitString[1].split("</redirectUrl>");
			redirectUrl = splitString[0];
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		listener.onTaskExecuted(redirectUrl);
		dialog.dismiss();
	}

}
