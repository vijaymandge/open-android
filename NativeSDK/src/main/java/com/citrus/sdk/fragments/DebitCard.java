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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.citrus.sdk.Constants;
import com.citrus.sdk.ErrorValidation;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.operations.GuestCheckout;
import com.citrus.sdk.operations.JSONUtils;
import com.citrus.sdk.operations.OneClicksignup;
import com.citrus.sdk.webops.Pay;
import com.citrus.sdk.activity.Web3DSecure;
import com.citruspay.mobile.payment.Card;
import com.citruspay.mobile.payment.JSONTaskComplete;
import com.citruspay.mobile.payment.internals.PaymentUtils;
import com.citruspay.mobile.payment.widgets.CardNumberEditText;
import com.citruspay.mobile.payment.widgets.ExpiryEditText;
import com.citruspay.util.HMACSignature;

import org.json.JSONException;
import org.json.JSONObject;

public class DebitCard extends Fragment{
	private CardNumberEditText cardnumber; 
	private ExpiryEditText expDate;
	private EditText nameOnCard, cvv;
	private Button submitButton;
	private Card card;
	private String paymentType;
	private JSONObject paymentObject;
	private JSONTaskComplete taskExecuted;
    private OneClicksignup oneClicksignup;

    private String cardNumStr, expDateStr, cvvStr, holder_name;

	private View returnView;
		
	public DebitCard() {
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		returnView = inflater.inflate(R.layout.activity_debit_card, container, false);

        Bundle data = getArguments();

        paymentType = data.getString(Constants.PAY_TYPE);

        initEditText();

        initSubmitButton();

		return returnView;
	}
	

	private void initEditText() {
		/*Pick these details in runtime*/
        cardnumber = (CardNumberEditText) returnView.findViewById(R.id.debitCardText);

		cardnumber.setText("4028530052708001");

        expDate = (ExpiryEditText) returnView.findViewById(R.id.cardExpiry);

        nameOnCard = (EditText) returnView.findViewById(R.id.nameOnCard);

        cvv = (EditText) returnView.findViewById(R.id.cvvText);

        submitButton = (Button) returnView.findViewById(R.id.submitButton);


	}
	
	private void initSubmitButton() {
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                initValues();
				if (isValidCard()) {
                    if (TextUtils.equals(paymentType, Constants.GUEST_FLOW)) {
                        createGuestTxn();
                    }
                    else {
                        savePayOption();
                        createMemberTxn();
                    }
				}
			}
		});
	}

    private void initValues() {
        cardNumStr = cardnumber.getText().toString().replace(" ", "");
        String[] dateStr = expDate.getText().toString().split("/");
        expDateStr = dateStr[0] + "/20" + dateStr[1];
        cvvStr = cvv.getText().toString();
        holder_name = nameOnCard.getText().toString();
        oneClicksignup = new OneClicksignup(getActivity());
    }
	
	protected boolean isValidCard() {
		card = new Card(cardNumStr, expDate.getText().toString(), cvvStr, holder_name);
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

    private void createGuestTxn() {
        GuestCheckout checkout = new GuestCheckout(getActivity());

        checkout.cardPay(PaymentUtils.DEBIT_CARD.toString(), JSONUtils.TXN_AMOUNT, card);

        oneClicksignup.oneclickSignUp(oneClicksignup.getSignupparams(), getPaymentObject(), "debit");
    }

	private void createMemberTxn() {
        String txnId = "CTXN" + String.valueOf(System.currentTimeMillis());
        String data = "merchantAccessKey=" + Constants.ACCESS_KEY + "&transactionId=" + txnId + "&amount=" + JSONUtils.TXN_AMOUNT;
        String signature = HMACSignature.generateHMAC(data, Constants.SECRET_KEY);

        insertValues(txnId, signature);
        initiateTxn();
	}
	
	private void insertValues(String txnId, String signature) {
		
		try {
            JSONObject amount = JSONUtils.fillinAmountDetails();
            JSONObject address = JSONUtils.fillinAddress();
            JSONObject userDetails = JSONUtils.fillinUserDetails(address);

            JSONObject paymentMode = new JSONObject();
            paymentMode.put("type", "debit");
            paymentMode.put("scheme", card.getCardType().toUpperCase());
            paymentMode.put("number", cardNumStr);
            paymentMode.put("holder", holder_name);
            paymentMode.put("expiry", expDateStr);
            paymentMode.put("cvv", cvvStr);

            JSONObject paymentToken = JSONUtils.fillinPaymentToken(paymentMode);

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
		taskExecuted = new JSONTaskComplete() {

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

    private void savePayOption() {
        oneClicksignup.saveCardOption(getPaymentObject());
    }

    public JSONObject getPaymentObject() {
        JSONObject paymentDetails = null;
        try {
            paymentDetails  =  new JSONObject().put("type", "debit").put("owner", nameOnCard.getText().toString())
                    .put("number", cardNumStr).put("expiryDate", expDate.getText().toString()).put("scheme", card.getCardType().toUpperCase());
        } catch (JSONException e) {

        }

        return paymentDetails;
    }
	
}
