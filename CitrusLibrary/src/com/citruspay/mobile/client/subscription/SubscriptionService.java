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




import android.text.TextUtils;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import com.citruspay.mobile.client.subscription.contact.ContactDetails;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.client.rest.RESTException;
import com.citruspay.mobile.payment.oauth2.AuthorizationUtil;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;
import com.citruspay.mobile.payment.oauth2.OAuth2Service;
/*
import org.apache.commons.codec.binary.Base64;
*/

public class SubscriptionService {

	private final OAuth2Service signup, signedin;
	private final RESTClient rest;
	private final PasswordGenerator password;
	private final HttpClient http;
	public static Cookie cookie = null;

	protected SubscriptionService(
			OAuth2Service signup, 
			OAuth2Service signedin,
			RESTClient rest,
			HttpClient http,
			PasswordGenerator password) {
		this.signup = signup;
		this.signedin = signedin;
		this.rest = rest;
		this.http = http;
		this.password = password;
	}

	/**
	 * Method for a new subscription.
	 * 
	 * @param email
	 *            the email of the new subscriber.
	 * @param mobile
	 *            the mobile number of the new subscriber.
	 * @param password
	 *            of new subscriber.
	 * @param firstName
	 *            name of new subscriber.
	 * @param lastName
	 *            name of new subscriber.
	 * @return the username of new subscriber.
	 * @throws JSONException 
	 */
	public String signup(
			String email, 
			String mobile, 
			String password,
			String firstName, 
			String lastName) throws ProtocolException, OAuth2Exception, SubscriptionException, JSONException {
		// signup
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		params.put("mobile", mobile);
		JSONObject profile = null;
		try {
			profile = rest.post(
					URI.create("identity/new"),
					AuthorizationUtil.asHeader(signup.getAuthorization()),
					params);
		} catch (RESTException rx) {
			handleRESTException(rx);
		}

		// signin
		String generated = this.password.generate(email, mobile);
		signin(email, generated);
		Collection<Header> authorization = AuthorizationUtil
				.asHeader(signedin.getAuthorization());

		// change password
		    params.put("old", generated);
        if (TextUtils.isEmpty(password)) {
            params.put("new", generated);
        }
        else {
            params.put("new", password);
        }

		try {
			rest.put(URI.create("identity/me/password"), authorization, params);
		} catch (RESTException rx) {
			handleRESTException(rx);
		}

		JSONObject update = null;
		ContactDetails contact = null;

		try {
			update = new JSONObject();
			update.put("type", "contact").put("email", email)
			.put("mobile", mobile).put("firstName", firstName)
			.put("lastName", lastName);
			contact = new ContactDetails();
			contact.parse(update);
		}
		catch (JSONException jx) {
				throw new RuntimeException(jx);
		}     


		updateProfile(contact);

		return profile.getString("username");
	}

	/**
	 * Method for a existing citrus member to Signin.
	 * 
	 * @param email
	 *            the email of the new subscriber.
	 * @param password
	 *            of existing member.
	 */
	public void signin(
			String email, 
			String password) throws ProtocolException, OAuth2Exception, SubscriptionException {
		signedin.signin(email, password);
		/*HttpGet get;
		*//*try {
			*//**//*get = new HttpGet(
					new URI("https://stg.citruspay.com/session/create"));
			byte[] encodedBytes = Base64.encodeBase64((email+":"+password).getBytes());
			//System.out.println("encodedBytes " + new String(encodedBytes));
			get.setHeader("Authorization", "Basic " + new String(encodedBytes));
			http.execute(get);

			List<Cookie> cookies = ((DefaultHttpClient)http).getCookieStore().getCookies();
			for (int i = 0; i < cookies.size(); i++) {
			    cookie = cookies.get(i);
			}

			String cookieString = cookie.getName() + "=" + cookie.getValue();
			signedin.storeCookie("cookie", cookieString);*//**//*
		}catch (ClientProtocolException cpe) {	
			throw new ProtocolException(cpe);
		} catch (IOException ioe) {
			throw new ProtocolException(ioe);
		} catch (URISyntaxException use) {
			throw new ProtocolException(use);
		}*/
	}

	/**
	 * Method to verify password of existing citrus member.
	 * @param username that encapsulates the emailId of existing citrus member
 	 * @param password of existing member.         
	 * 
	 * @return JSONObject that encapsulates the message on verification.
	 * @throws SubscriptionException 
	 * @throws ProtocolException 
	 */
	public String verifyPassword(String password) throws ProtocolException, OAuth2Exception, SubscriptionException, JSONException{
		Map<String, String> params = new HashMap<String, String>();
		params.put("password", password);
		JSONObject profile = null;
		try {
			profile = rest.post(
					URI.create("identity/me/password"),
					AuthorizationUtil.asHeader(signedin.getAuthorization()),
					params);
		} catch (RESTException rx) {
			handleRESTException(rx);
		}

			return profile.getString("verified");
	}

	/**
	 * Method for an existing citrus member to get Profile.
	 * 
	 * @return JSONObject that encapsulates the profile of existing member.
	 */
	public ProfileElement getProfile(ProfileElement element) 
			throws ProtocolException, OAuth2Exception, SubscriptionException {
		try {
			// get profile element
			element.parse(rest.get(
					URI.create("profile/me/" + element.classifier()),
					AuthorizationUtil.asHeader(signedin.getAuthorization())));
		} catch (RESTException rx) {
			handleRESTException(rx);
		}
		return element;
	}

	/**
	 * Method for an existing citrus member to Update Profile.
	 * 
	 * @param profile
	 *            that encapsulates the updated profile of existing member.
	 */
	public void updateProfile(ProfileElement element) 
			throws ProtocolException, OAuth2Exception, SubscriptionException {
		try {
			rest.put(URI.create("profile/me/" + element.classifier()),
					AuthorizationUtil.asHeader(signedin.getAuthorization()),
					element.asJSON());
		} catch (RESTException rx) {
			handleRESTException(rx);
		}
	}



	public void resetPassword(String username) throws ProtocolException, OAuth2Exception, SubscriptionException{
		try {
			rest.post(
					URI.create("identity/passwords/reset"),
					AuthorizationUtil.asHeader(signup.getAuthorization()),
					Collections.singletonMap("username", username));
		}catch(RESTException rx){
			handleRESTException(rx);
		}
	}

	private JSONObject handleRESTException(RESTException rx) throws ProtocolException, SubscriptionException {
		if (rx.getHttpStatusCode() == HttpStatus.SC_BAD_REQUEST) {
			throw new SubscriptionException(rx, rx.getContent());
		} else {
			throw new ProtocolException(rx);
		}
	}
}