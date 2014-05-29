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

import com.citruspay.mobile.payment.client.rest.ProtocolException;


public interface OAuth2Service {
	OAuth2Token signin(String username, Object credential) throws ProtocolException, OAuth2Exception;
	OAuth2Token getAuthorization() throws ProtocolException, OAuth2Exception;
	public void storeCookie(String key, String cookie);
}
