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

package com.citrus.sdk.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


import com.citrus.sdk.Constants;
import com.citrus.sdk.ErrorValidation;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.operations.GuestCheckout;
import com.citrus.sdk.webops.GetSignedorder;
import com.citrus.sdk.webops.GuestPay;
import com.citrus.sdk.webops.Pay;
import com.citrus.sdk.webops.SavePayOption;
import com.citrus.sdk.webops.Web3DSecure;
import com.citruspay.mobile.payment.Card;
import com.citruspay.mobile.payment.OnTaskCompleted;
import com.citruspay.mobile.payment.internals.PaymentUtils;
import com.citruspay.mobile.payment.widgets.CardNumberEditText;
import com.citruspay.mobile.payment.widgets.ExpiryEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class CreditCard extends Fragment{
	private CardNumberEditText cardnumber; 

    private ExpiryEditText expDate;

    private EditText nameOnCard, cvv;

    private Button submitButton;

    private Card card;
	
	private JSONObject paymentObject;

    private OnTaskCompleted taskExecuted;
	
	private View returnView;

    private Activity activity;

    private String paymentType;

    private CheckBox checkBox;
	
	public CreditCard() {
		
	}

    public static CreditCard newInstance(String flowType) {
        return new CreditCard();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		returnView = inflater.inflate(R.layout.activity_credit_card, container, false);
		Bundle data = getArguments();
        paymentType = data.getString(Constants.PAY_TYPE);
        initEditText();
		return returnView;
	}

	private void initEditText() {
		cardnumber = (CardNumberEditText) returnView.findViewById(R.id.creditCardText);
		cardnumber.setText("4111111111111111");
		expDate = (ExpiryEditText) returnView.findViewById(R.id.cardExpiry);
		expDate.setText("12/20");
		nameOnCard = (EditText) returnView.findViewById(R.id.nameOnCard);
		nameOnCard.setText("Testing Here");
		cvv = (EditText) returnView.findViewById(R.id.cvvText);
		cvv.setText("303");

        checkBox = (CheckBox) returnView.findViewById(R.id.saveOption);

		submitButton = (Button) returnView.findViewById(R.id.submitButton);
		initSubmitButton();
	}
	
	private void initSubmitButton() {
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isValidCard()) {
                    if (TextUtils.equals(paymentType, Constants.GUEST_FLOW)) {
                        createGuestTxn();
                    }
                    else {
                        if (checkBox.isChecked()) {
                            savePayOption();
                        }
                        createMemberTxn();
                    }
				}
			}
		});
	}
	
	protected boolean isValidCard() {
		card = new Card(cardnumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString(), nameOnCard.getText().toString());			
		if (!card.validateNumber()) {
			cardnumber.requestFocus();
			ErrorValidation.showError(getActivity().getApplicationContext(), cardnumber, "Invalid Credit Card");
			return false;
		}
		
		if (!card.validateExpiryDate()) {
			expDate.requestFocus();
			ErrorValidation.showError(getActivity().getApplicationContext(), expDate, "Invalid Expiry Date");
			return false;
		}
		
		if (!card.validateCVC()) {
			cvv.requestFocus();
			ErrorValidation.showError(getActivity().getApplicationContext(), cvv, "Invalid CVV");
			return false;
		}
		
		return true;
	}
	
	private void createMemberTxn() {
		JSONObject txnDetails = new JSONObject();
		try {
				txnDetails.put("amount", "1.21");
				txnDetails.put("currency", "INR");
				txnDetails.put("redirect", Constants.REDIRECT_URL);
		} catch (JSONException e) {
				
		}
			
		new GetSignedorder(getActivity(), txnDetails, new OnTaskCompleted() {

				@Override
				public void onTaskExecuted(JSONObject[] signedOrder, String message) {
					try {
						String txnId = signedOrder[0].getString("merchantTransactionId");
						String signature = signedOrder[0].getString("signature");
						insertValues(txnId, signature);
						initiateTxn();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
		}).execute();
	}
	
	private void insertValues(String txnId, String signature) {
		
		try {
			//*Payment Details - DO NOT STORE THEM LOCALLY OR ON YOUR SERVER*//*
			JSONObject amount = new JSONObject();
			amount.put("currency", "INR");
			amount.put("value", "1.21");
			
			JSONObject address = new JSONObject();
			address.put("street1", "");
			address.put("street2", "");
			address.put("city", "Mumbai");
			address.put("state", "Maharashtra");
			address.put("country", "India");
			address.put("zip", "411046");
			
			JSONObject userDetails = new JSONObject();
			userDetails.put("email", "tester@gmail.com");
			userDetails.put("firstName", "Shardul");
			userDetails.put("lastName", "Swwww");
			userDetails.put("mobileNo", "7875432990");
			
			userDetails.put("address", address);
			
			JSONObject paymentMode = new JSONObject();
			paymentMode.put("type", "credit");
			paymentMode.put("scheme", "VISA");
			paymentMode.put("number", "4111111111111111");
			paymentMode.put("holder", "Tester Shardul");
			paymentMode.put("expiry", "12/2020");
			paymentMode.put("cvv", "100");
			
			JSONObject paymentToken = new JSONObject();
			paymentToken.put("type", "paymentOptionToken");
			
			paymentToken.put("paymentMode", paymentMode);
			
			paymentObject = new JSONObject();
			paymentObject.put("merchantTxnId", txnId);
			paymentObject.put("paymentToken", paymentToken);
			paymentObject.put("userDetails", userDetails);
			paymentObject.put("amount", amount);
			paymentObject.put("notifyUrl", "");
			paymentObject.put("merchantAccessKey", Constants.ACCESS_KEY);
			paymentObject.put("requestSignature", signature);
			paymentObject.put("returnUrl", Constants.REDIRECT_URL);
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
						Intent intent = new Intent(getActivity().getApplicationContext(), Web3DSecure.class);
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
	
	protected void createGuestTxn() {
        GuestCheckout checkout = new GuestCheckout(getActivity());
        checkout.cardPay(PaymentUtils.CREDIT_CARD.toString());
	}

    private void savePayOption() {
        JSONObject paymentDetails = null;
        try {
          String cardNum = cardnumber.getText().toString().replace(" ", "");
          paymentDetails  =  new JSONObject().put("type", "credit").put("owner", nameOnCard.getText().toString())
                    .put("number", cardNum).put("expiryDate", expDate.getText().toString()).put("scheme", card.getCardType().toUpperCase()).put("bank", "");
        } catch (JSONException e) {

        }
        new SavePayOption(getActivity(), paymentDetails).execute();

    }
	
}
