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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ProfileDiff {
	private String name;
	private JSONObject profile;
	private List<String> elements = new ArrayList<String>();

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	public List<String> getElements() {
		return elements;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getProfile() {
		return profile;
	}

	public void setProfile(JSONObject profile) {
		this.profile = profile;
	}

}
