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


/* This response consists of transaction status and other details.
*  You can log these transactions and their status.
* */
package com.citrus.sdk.interfaces;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class JSInterface {
	private Activity activity;

	public JSInterface(Activity activity) {
		this.activity = activity;
	}
	
	public void pgResponse(String response) {

		try {
			JSONObject response_json = new JSONObject(response);
			Toast.makeText(activity.getApplicationContext(), response_json.getString("TxStatus"), Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public String getJS(String otpValue) {
        String js = "javascript: " +
                    "var inputs = document.querySelectorAll('input[type=password]');" +
                    "var forms  = document.getElementsByTagName('form');" +
                    "inputs[inputs.length - 1].value='"+otpValue+"';" +
                    "forms[forms.length - 1].submit();";
        return js;
    }
	
}