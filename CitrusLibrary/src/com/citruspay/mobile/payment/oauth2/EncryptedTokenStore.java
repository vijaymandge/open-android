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
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Enumeration;
import java.util.Properties;

import javax.crypto.Cipher;

public class EncryptedTokenStore extends PropertiesFileTokenStore {

	public interface Base64 {
		String encode(byte[] input);
		byte[] decode(String input);
	}
	
	private final Key key;
	private final Cipher cipher;
	private final Base64 base64;
	
	public EncryptedTokenStore(Cipher cipher, Key key, File store, Base64 base64) {
		super(store);
		this.key = key;
		this.cipher = cipher;
		this.base64 = base64;
	}

	@Override
	protected void storeAsProperties(Properties tokens) {
		// encrypt property values
		Properties ep = new Properties();
		for (Enumeration<?> names = tokens.propertyNames(); names.hasMoreElements(); ) {
			String name = (String)names.nextElement();
			ep.setProperty(name, encrypt(tokens.getProperty(name)));
		}
		
		// store
		super.storeAsProperties(ep);
	}

	@Override
	protected Properties loadAsProperties() {
		// load
		Properties ep = super.loadAsProperties();
		
		// decrypt property values
		Properties tokens = new Properties();
		for (Enumeration<?> names = ep.propertyNames(); names.hasMoreElements(); ) {
			String name = (String)names.nextElement();
			tokens.setProperty(name, decrypt(ep.getProperty(name)));
		}
		return tokens;
	}

	protected String decrypt(String input) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(base64.decode(input)));
		} catch (GeneralSecurityException gsx) {
			throw new RuntimeException(gsx);
		}
	}

	protected String encrypt(String input) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return base64.encode(cipher.doFinal(input.getBytes())).trim();
		} catch (GeneralSecurityException gsx) {
			throw new RuntimeException(gsx);
		}
	}
}
