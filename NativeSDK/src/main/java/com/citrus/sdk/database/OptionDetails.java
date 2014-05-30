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

package com.citrus.sdk.database;

import android.database.Cursor;

public class OptionDetails {
		
	private String mmid;
	
	private String scheme;
	
	private String expiryDate;
	
	private String name;
	
	private String owner;
	
	private String bank;
	
	private String number;
	
	private String type;
	
	private String impsRegisteredMobile;
	
	private String tokenId;
	
	public OptionDetails(Cursor current_cursor) {
		this.mmid = current_cursor.getString(0);
		this.scheme = current_cursor.getString(1);
		this.expiryDate = current_cursor.getString(2);
		this.name = current_cursor.getString(3);
		this.owner = current_cursor.getString(4);
		this.bank = current_cursor.getString(5);
		this.number = current_cursor.getString(6);
		this.type = current_cursor.getString(7);
		this.impsRegisteredMobile = current_cursor.getString(8);
		this.tokenId = current_cursor.getString(9);
	}
	
	public String getMMID() {
		return this.mmid;
	}
	
	public String getScheme() {
		return this.scheme;
	}
	
	public String getExpDate() {
		return this.expiryDate;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public String getBank() {
		return this.bank;
	}
	
	public String getNumber() {
		return this.number;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String impsInfo() {
		return this.impsRegisteredMobile;
	}
	
	public String getToken() {
		return this.tokenId;
	}
}
