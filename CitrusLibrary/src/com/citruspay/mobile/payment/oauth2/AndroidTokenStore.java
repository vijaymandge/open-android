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


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AndroidTokenStore implements OAuth2TokenStore {
	public static final String STORED_VALUES = "StoredValues";

	private Activity mActivity;
	private SharedPreferences tokenPrefs;
	
	
	
	public AndroidTokenStore(Activity mContext) {
		this.mActivity = mContext;
		tokenPrefs = this.mActivity.getSharedPreferences(STORED_VALUES, 0);
	}
	
	@Override
	public void store(String key, OAuth2Token token) {
		Editor editor = tokenPrefs.edit();
		editor.putString(key, token.asJSON().toString());
		editor.commit();
	}

	@Override
	public boolean contains(String key) {
		return (tokenPrefs.contains(key));
	}

	@Override
	public OAuth2Token load(String key) {
		String token = tokenPrefs.getString(key, null);
	    JSONObject tokenObject = null;
		try {
			tokenObject = new JSONObject(token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    return OAuth2Token.create(tokenObject);
		
	}

	public void clearToken(String key, boolean clearData) {
		Editor editor = tokenPrefs.edit();
		editor.putString(key, null);
		editor.commit();
	}
		
}
