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

import com.citrus.mobile.client.load.NetCardService;
import com.citrus.mobile.client.load.PrepaidException;
import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.payment.OnTaskCompleted;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

import org.json.JSONException;
import org.json.JSONObject;

public class GetSignedorder extends AsyncTask<Void, Void, JSONObject[]>{
	private MobileClient mobileClient;
	private NetCardService cardService;
	private String executionMsg = null;
	private OnTaskCompleted listener;
	private Activity activity;
	private JSONObject txnObject;
	private ProgressDialog dialog;
	
	public GetSignedorder(Activity activity, JSONObject txnObject, OnTaskCompleted listener) {
		this.listener = listener;
		this.activity = activity;
		this.txnObject = txnObject;
	}
	
	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(activity, "Please Wait", "Signing your order.", true, true);
	}
	
	@Override
	protected JSONObject[] doInBackground(Void... params) {
		JSONObject txnOrder = null;
		mobileClient = new MobileClient(activity);
		cardService = mobileClient.getNetCardService(Constants.SignInpClientId, Constants.SignInClientSecret);
		try {
            txnOrder = new JSONObject();
			txnOrder = cardService.getLoadValues(txnObject);
		} catch (ProtocolException e) {
			e.printStackTrace();
			executionMsg = "proto";
		} catch (OAuth2Exception e) {
			e.printStackTrace();
			executionMsg = "oauth";
		} catch (PrepaidException e) {
			e.printStackTrace();
			executionMsg = "prepaid";
		} catch (JSONException e) {
			e.printStackTrace();
			executionMsg = "json";
		}
		return new JSONObject[]{txnOrder};
	}
	
	@Override
	protected void onPostExecute(JSONObject[] result) {
		listener.onTaskExecuted(result, executionMsg);
		dialog.dismiss();
	}

}
