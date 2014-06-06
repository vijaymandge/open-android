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

import com.citrus.sdk.Constants;
import com.citruspay.mobile.payment.OnTaskCompleted;
import com.citruspay.mobile.payment.PaymentGateway;

import org.json.JSONObject;

public class Pay extends AsyncTask<Void, Void, Void> {
	private JSONObject urlDetails;
	private OnTaskCompleted taskExecuted;
	private JSONObject postObject;
	private PaymentGateway makePayments;
	private String result;
	private Activity activity;
	private ProgressDialog waitBox;
	public Pay(Activity activity, JSONObject postObject, OnTaskCompleted taskExecuted) {
		this.postObject = postObject;
		this.taskExecuted = taskExecuted;	
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		waitBox = ProgressDialog.show(activity, "Please Wait", "Connecting to the PG", true, true);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		urlDetails = null;		
		try {
			makePayments = new PaymentGateway(Constants.CITRUS_SERVER_URL, Constants.ACCESS_KEY, postObject);
			urlDetails = makePayments.postPayment();
		} catch (Exception e) {
			result = e.toString();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		taskExecuted.onTaskExecuted(new JSONObject[]{urlDetails}, this.result);
		waitBox.dismiss();
	}
	
}