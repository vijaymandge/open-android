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
/** 
 * This asynchtask is meant for signing the customer in and getting an OAuth Token. 
 * Using this token, you can get user details such as 
 * 1) Saved Payment options including credit, debit and net banking
 * 2) Default Payment Options set by user.
 * 3) Contact details of the users - which you can save locally in shared preferences.
 * 
 * You need to sign the user in, if you get an OAuth Exception.
 **/

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.SubscriptionException;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.client.subscription.contact.ContactDetails;
import com.citruspay.mobile.client.subscription.payment.PaymentConfiguration;
import com.citruspay.mobile.payment.JSONTaskComplete;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

import org.json.JSONObject;

public class GetCustprofile extends AsyncTask<Void, Void, JSONObject[]> {
	private JSONTaskComplete listener;
	private Activity activity;
	
	private MobileClient mobileClient;
	private SubscriptionService service;
	
	private String executionMsg = null;
		
	public GetCustprofile(Activity activity, JSONTaskComplete listener) {
		this.listener = listener;
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected JSONObject[] doInBackground(Void... params) {
		JSONObject paymentOptions = new JSONObject();
		JSONObject contactDetails = new JSONObject();
		mobileClient = new MobileClient(activity, Constants.CITRUS_OAUTH_URL);
		service = mobileClient.getSubscriptionService(Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET, Constants.SIGNIN_ID, Constants.SIGNIN_SECRET);
		try {
			PaymentConfiguration payment = new PaymentConfiguration();			
			paymentOptions = service.getProfile(payment).asJSON();
			ContactDetails contact = new ContactDetails();
			contactDetails = service.getProfile(contact).asJSON();
            executionMsg = "success";
		} catch (ProtocolException e) {
            executionMsg = "proto";
		} catch (OAuth2Exception e) {
			executionMsg = "oauth";
		} catch (SubscriptionException e) {
            executionMsg = "subsc";
		}
		return new JSONObject[]{contactDetails, paymentOptions};
	}
	
	@Override
	protected void onPostExecute(JSONObject result[]) {
		listener.onTaskExecuted(result, executionMsg);
	}

}
