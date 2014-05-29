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

package com.citruspay.mobile.client.subscription;

import org.json.JSONObject;

public class JSONProfileElement implements ProfileElement {

	private final String classifier;
	protected JSONObject json = new JSONObject();

	protected JSONProfileElement(String classifier) {
		this.classifier = classifier;
	}

	@Override
	public String classifier() {
		return classifier;
	}
	
	@Override
	public JSONObject asJSON() {
		return json;
	}
	
	@Override
	public void parse(JSONObject json) {
		this.json = json;
	}

}
