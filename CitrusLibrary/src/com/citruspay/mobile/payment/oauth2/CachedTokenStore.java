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

public class CachedTokenStore extends InMemoryTokenStore {

	private final OAuth2TokenStore cached;

	public CachedTokenStore(OAuth2TokenStore cached) {
		this.cached = cached;
	}

	@Override
	public void store(String key, OAuth2Token token) {
		super.store(key, token);
		cached.store(key, token);
	}

	@Override
	public boolean contains(String key) {
		return super.contains(key) || cached.contains(key);
	}

	@Override
	public OAuth2Token load(String key) {
		if (super.contains(key)) {
			return super.load(key);
		}
		OAuth2Token token = cached.load(key);
		super.store(key, token);
		return token;
	}
	
}
