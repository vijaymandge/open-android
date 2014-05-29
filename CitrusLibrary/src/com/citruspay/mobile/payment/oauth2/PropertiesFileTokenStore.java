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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

public class PropertiesFileTokenStore implements OAuth2TokenStore {

	private final File store;

	public PropertiesFileTokenStore(File store) {
		super();
		this.store = store;
	}

	@Override
	public void store(String key, OAuth2Token token) {
		// update store
		Properties tokens = loadAsProperties();
		tokens.setProperty(key, token.asJSON().toString());
		storeAsProperties(tokens);
	}

	@Override
	public boolean contains(String key) {
		return loadAsProperties().containsKey(key);
	}

	@Override
	public OAuth2Token load(String key) {
		try {
			return OAuth2Token.create(new JSONObject(loadAsProperties().getProperty(key)));
		} catch (JSONException jx) {
			throw new RuntimeException(jx);
		}
	}

	protected void storeAsProperties(Properties tokens) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(store);
			tokens.store(os, "");
		} catch (IOException iox) {
			throw new RuntimeException(iox);
		} finally {
			if (os != null) {
				try { os.close(); } catch (IOException x) {/* ignore */}
			}
		}
	}
	
	protected Properties loadAsProperties() {
		Properties props = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(store);
			props.load(is);
		} catch (IOException iox) {
			throw new RuntimeException(iox);
		} finally {
			if (is != null) {
				try { is.close(); } catch (IOException x) {/* ignore */}
			}
		}
		return props;
	}
	
}
