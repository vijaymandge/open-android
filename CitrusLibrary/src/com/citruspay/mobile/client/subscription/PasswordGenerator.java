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

/**
 * Generates a default password at user subscription time.
 *
 * When a new user account is created, the system assigns it a default password
 * and that default password must be shared between the client application
 * requesting the account creation and the server creating the account.
 * PasswordGenerator is the way to share the default password between client
 * application and server.
 */
public interface PasswordGenerator {

	/**
	 * Generates the default password for a new subscription.
	 * 
	 * @param email the email of the new subscriber.
	 * @param mobile the mobile number of the new subscriber.
	 * @return the default password for the new subscriber.
	 */

	String generate(String email, String mobile);
}
