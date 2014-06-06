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

package com.citrus.sdk.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.Constants;
import com.citrus.sdk.PaymentAdapter;
import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.webops.GetCustprofile;
import com.citrus.sdk.webops.GetSignedorder;
import com.citrus.sdk.webops.Pay;
import com.citrus.sdk.webops.SignInAsynch;
import com.citrus.sdk.webops.Web3DSecure;
import com.citruspay.mobile.client.subscription.OptionDetails;
import com.citruspay.mobile.payment.OnTaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SavedOptions extends Fragment{
	private View returnView;
	
	private ProgressBar spinner;
	
	private OnTaskCompleted taskExecuted, listener, signInListener;
	
	private PaymentAdapter adapter;
	
	private ListView payOptionList;
	
	private JSONObject paymentObject;
	
	private RelativeLayout signInLayout;
	
	private EditText usernameET, passwordET;
	
	private TextView errorText;
	
	public SavedOptions() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		returnView = inflater.inflate(R.layout.activity_saved_options, container, false);
		listener = new OnTaskCompleted() {

			@Override
			public void onTaskExecuted(JSONObject[] returnObject,
					String message) {
				spinner.setVisibility(View.INVISIBLE);
				if (TextUtils.equals(message, "oauth")) {
					Toast.makeText(getActivity().getApplicationContext(), "User is not signed in!", Toast.LENGTH_SHORT).show();
					showSignInFlow("User not signed in");
				}
				else {					
					initViews();
					storePayOptions(returnObject[1]);
					showPayOptions();
				}
			}

			
		};
		
		signInListener = new OnTaskCompleted() {
			
			@Override
			public void onTaskExecuted(JSONObject[] paymentObject, String message) {
				spinner.setVisibility(View.INVISIBLE);
				if (TextUtils.equals(message, "signedIn")) {
					showSavedOptions();
				}
				else {
					showSignInFlow("User not signed in");
				}
			}
		};
		
		showSavedOptions();
		return returnView;
	}
	
	private void initViews() {
		payOptionList = (ListView) returnView.findViewById(R.id.payOptions);
		payOptionList.setVisibility(View.VISIBLE);
	}
	
	protected void storePayOptions(JSONObject savedOption) {
		DBHandler dbInstance = new DBHandler(getActivity().getApplicationContext());
		try {
			JSONArray payOptionArray = savedOption.getJSONArray("paymentOptions");
			dbInstance.addPayOption(payOptionArray);
            dbInstance.setDefaultOption("");
            dbInstance.setDefaultOption(savedOption.getString("defaultOption"));
		} catch (JSONException e) {
			Toast.makeText(getActivity().getApplicationContext(), "Could not add payment options!", Toast.LENGTH_SHORT).show();
		} finally {
			dbInstance.close();
		}


	}
	
	private void showPayOptions() {
		adapter = new PaymentAdapter(getActivity());
		payOptionList.setAdapter(adapter);
		setOnClick();
	}

	private void setOnClick() {
		payOptionList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				createTransaction(adapter.payList.get(position));
			}
		});
	}

	private void showSignInFlow(String errorMessage) {
		signInLayout = (RelativeLayout) returnView.findViewById(R.id.signInLayout);
		signInLayout.setVisibility(View.VISIBLE);
		usernameET = (EditText) returnView.findViewById(R.id.username);
		passwordET = (EditText) returnView.findViewById(R.id.password);
		errorText= (TextView) returnView.findViewById(R.id.errorText);
		errorText.setText(errorMessage);
		Button signIn = (Button) returnView.findViewById(R.id.signIn);
		signIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = usernameET.getText().toString();
				String password = passwordET.getText().toString();	
				String params[] = new String[]{username, password};
				signInLayout.setVisibility(View.INVISIBLE);
				spinner.setVisibility(View.VISIBLE);
				new SignInAsynch(getActivity(), signInListener).execute(params);
			}
		});
	}

	
	private void showSavedOptions() {
			spinner = (ProgressBar) returnView.findViewById(R.id.progressBar1);
			spinner.setVisibility(View.VISIBLE);
			getSavedOptions();
	}

	private void getSavedOptions() {
		new GetCustprofile(getActivity(), listener).execute();
	}
	
	protected void createTransaction(OptionDetails optionDetails) {
		/*Get a signed order first*/
		final String token = optionDetails.getToken();
		final String type = optionDetails.getType();
		JSONObject txnDetails = new JSONObject();
		try {
				txnDetails.put("amount", "1");
				txnDetails.put("currency", "INR");
				txnDetails.put("redirect", Constants.REDIRECT_URL);
		} catch (JSONException e) {
				
		}
			
		new GetSignedorder(getActivity(), txnDetails, new OnTaskCompleted() {

				@Override
				public void onTaskExecuted(JSONObject[] signedOrder, String message) {
					if (TextUtils.isEmpty(message)) {
						try {
							String txnId = signedOrder[0].getString("merchantTransactionId");
							String signature = signedOrder[0].getString("signature");
							if (TextUtils.equals(type, "NETBANKING")) {
								fillDetails(txnId, signature, token, "");
							}
							else {
								processCardFlow(txnId, signature, token);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}
				
		}).execute();
	}
		
	private void processCardFlow(final String txnId, final String signature, final String token) {
		final Dialog cvvDialog = new Dialog(getActivity());
		LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cvvDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.cvv, null);
		final EditText cvvEditText = (EditText)view.findViewById(R.id.cvvText);
		final TextView errorText = (TextView)view.findViewById(R.id.errorText);
		Button payButton = (Button)view.findViewById(R.id.payButton);
		payButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(cvvEditText.getText().toString()) || cvvEditText.getText().toString().length() < 3) {
					errorText.setText("Invalid cvv");
				}
				else {
					fillDetails(txnId, signature, token, cvvEditText.getText().toString());
					cvvDialog.dismiss();
				}
			}
		});
		cvvDialog.setContentView(view);
		cvvDialog.show();
	}

	private void fillDetails(String txnId, String signature, String token, String cvv) {
		try {
			JSONObject amount = new JSONObject();
			amount.put("currency", "INR");
			amount.put("value", "1");
			
			JSONObject address = new JSONObject();
			address.put("street1", "");
			address.put("street2", "");
			address.put("city", "Mumbai");
			address.put("state", "Maharashtra");
			address.put("country", "India");
			address.put("zip", "411046");
			
			JSONObject userDetails = new JSONObject();
			userDetails.put("email", "tester@gmail.com");
			userDetails.put("firstName", "Tester");
			userDetails.put("lastName", "Testing");
			userDetails.put("mobileNo", "7875432990");
			
			userDetails.put("address", address);
			
			JSONObject paymentToken = new JSONObject();
			paymentToken.put("type", "paymentOptionIdToken");
			paymentToken.put("id", token);
			if (!TextUtils.isEmpty(cvv)) {
			paymentToken.put("cvv", cvv);
			}
			
			paymentObject = new JSONObject();
			paymentObject.put("merchantTxnId", txnId);
			paymentObject.put("paymentToken", paymentToken);
			paymentObject.put("userDetails", userDetails);
			paymentObject.put("amount", amount);
			paymentObject.put("notifyUrl", "");
			paymentObject.put("merchantAccessKey", Constants.ACCESS_KEY);
			paymentObject.put("requestSignature", signature);
			paymentObject.put("returnUrl", Constants.REDIRECT_URL);
			initiateTxn();
		} catch (JSONException e) {
			
		}
	}
	
	private void initiateTxn() {
		taskExecuted = new OnTaskCompleted() {

			@Override
			public void onTaskExecuted(JSONObject[] paymentObject, String message) {
				if (TextUtils.isEmpty(message)) {
					try {
						String url = paymentObject[0].getString("redirectUrl");
						Intent intent = new Intent(getActivity(), Web3DSecure.class);
						intent.putExtra("redirectUrl", url);
						startActivity(intent);	
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			
		};
		new Pay(getActivity(), paymentObject, taskExecuted).execute();
	}
}
