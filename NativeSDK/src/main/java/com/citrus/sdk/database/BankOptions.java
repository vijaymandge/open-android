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

public class BankOptions {
	private String bankName, bank_cid;

    public BankOptions(String bankName, String bank_cid) {
        this.bankName = bankName;
        this.bank_cid = bank_cid;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankcid() {
        return this.bank_cid;
    }
}
