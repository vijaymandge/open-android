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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.citrus.sdk.Constants;
import com.citrus.sdk.database.BankOptions;
import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.operations.GuestCheckout;
import com.citrus.sdk.operations.JSONUtils;
import com.citrus.sdk.operations.ManageOptions;
import com.citrus.sdk.operations.OneClicksignup;
import com.citrus.sdk.webops.Pay;
import com.citrus.sdk.activity.Web3DSecure;
import com.citruspay.mobile.payment.OnTaskCompleted;
import com.citruspay.util.HMACSignature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Netbanking extends Fragment {
	private View returnView;
	
	private Spinner spinner;
	
	private Map<String, String> bankOptions;

    private List<BankOptions> netbankList;
	
	private ArrayAdapter<String> dataAdapter;
	
	private List<String> bankNames, bankCodes;
	
	private String selectedBank, paymentType, selectedCode;
	
	private Button submit;

	private OnTaskCompleted taskExecuted, onNetbankfetched;

    private JSONObject paymentObject;

    private OneClicksignup oneClicksignup;

    private ManageOptions myOptions;
	
	public Netbanking() {
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		returnView = inflater.inflate(R.layout.activity_net_banking, container, false);

        selectedBank = "";

        paymentType = this.getArguments().getString(Constants.PAY_TYPE);

        oneClicksignup = new OneClicksignup(getActivity());

        myOptions = new ManageOptions(getActivity());

        initListener();

		initBanks();

        return returnView;
	}

    private void initListener() {
        onNetbankfetched = new OnTaskCompleted() {
            @Override
            public void onTaskExecuted(JSONObject[] options, String message) {

                try {
                    JSONArray array = options[0].getJSONArray("netBanking");
                    myOptions.storeBanks(array);
                    initBanks();
                } catch (JSONException e) {
                    return;
                }

            }
        };
    }

	private void initBanks() {

        try {
            netbankList = myOptions.getBanks();
        } catch (Exception e) {
            return;
        }

        if (netbankList.size() == 0) {
            myOptions.getStoreBanks(onNetbankfetched);
        }

        else {
            initViews();
        }

	}


	private void initViews() {
		bankNames = new ArrayList<String>();
		bankCodes = new ArrayList<String>();

        for (int i = 0; i < netbankList.size(); i++) {
            String bankname = netbankList.get(i).getBankName();
            String bankcid = netbankList.get(i).getBankcid();
            bankNames.add(bankname);
            bankCodes.add(bankcid);
        }

		dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bankNames);
		spinner = (Spinner) returnView.findViewById(R.id.bankOptions);
		spinner.setAdapter(dataAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedBank = bankNames.get(arg2);
                selectedCode = bankCodes.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedBank = "";
			}
		});
		
		initSubmit();
	}

	private void initSubmit() {
		submit = (Button) returnView.findViewById(R.id.submitButton);

		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

		    if (!TextUtils.isEmpty(selectedBank)) {
                try {
                    if (TextUtils.equals(paymentType, Constants.GUEST_FLOW)) {
                        createGuestTxn();
                    }
                    else {
                        savePayOption(selectedBank);
                        createMemberTxn();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Server may be down.", Toast.LENGTH_LONG).show();
                }

            }

			}
		});
	}

    private void createGuestTxn() {
        GuestCheckout checkout = new GuestCheckout(getActivity());

        checkout.netbankPay(selectedCode, JSONUtils.TXN_AMOUNT);

        oneClicksignup.oneclickSignUp(oneClicksignup.getSignupparams(), getPaymentObject(selectedBank), "netbanking");
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
            paymentMode.put("type", "netbanking");
            paymentMode.put("bank", "");
            paymentMode.put("code", selectedCode);

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


    private void savePayOption(String bankName) {
        oneClicksignup.savebankOption(getPaymentObject(bankName));
    }

    private JSONObject getPaymentObject(String bankName) {
        JSONObject paymentDetails = null;
        try {
            paymentDetails =  new JSONObject().put("bank", bankName);
        } catch (JSONException e) {

        }

        return paymentDetails;
    }

}
