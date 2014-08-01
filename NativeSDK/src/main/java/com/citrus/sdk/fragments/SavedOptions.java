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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
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
import com.citrus.sdk.activity.HomeScreen;
import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.operations.JSONUtils;
import com.citrus.sdk.operations.OneClicksignup;
import com.citrus.sdk.webops.GetCustprofile;
import com.citrus.sdk.webops.GetSign;
import com.citrus.sdk.webops.Pay;
import com.citrus.sdk.webops.ResetPassword;
import com.citrus.sdk.webops.SignInAsynch;
import com.citrus.sdk.activity.Web3DSecure;
import com.citruspay.mobile.client.Logout;
import com.citruspay.mobile.client.subscription.OptionDetails;
import com.citruspay.mobile.payment.BooleanTask;
import com.citruspay.mobile.payment.JSONTaskComplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SavedOptions extends Fragment{
    private View returnView;

    private ProgressBar progressBar;

    private JSONTaskComplete taskExecuted, listener, signInListener;

    private PaymentAdapter adapter;

    private ListView payOptionList;

    private JSONObject paymentObject;

    private RelativeLayout signInLayout;

    private EditText usernameET, passwordET;

    private TextView errorText, resetPass;

    public SavedOptions() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        returnView = inflater.inflate(R.layout.activity_saved_options, container, false);
        listener = new JSONTaskComplete() {

            @Override
            public void onTaskExecuted(JSONObject[] returnObject,
                                       String message) {
                progressBar.setVisibility(View.INVISIBLE);
                if (TextUtils.equals(message, "success")) {
                    initViews();
                    try {
                        storePayOptions(returnObject[1]);
                        showPayOptions();
                    } catch (Exception e) {
                        return;
                    }
                }
                else {
                    Logout.logoutUser(getActivity());
                    showSignInFlow("User not signed in");
                }
            }


        };

        signInListener = new JSONTaskComplete() {

            @Override
            public void onTaskExecuted(JSONObject[] paymentObject, String message) {
                progressBar.setVisibility(View.INVISIBLE);
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

        resetPass = (TextView) returnView.findViewById(R.id.resetPass);

        passwordET.requestFocus();

        String udata="Reset Password?";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        resetPass.setText(content);

        usernameET.setText(OneClicksignup.getDefaultGmail(getActivity()));
        errorText.setText(errorMessage);
        Button signIn = (Button) returnView.findViewById(R.id.signIn);
        signIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                String params[] = new String[]{username, password};
                signInLayout.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new SignInAsynch(getActivity(), signInListener).execute(params);
            }
        });

        resetPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
    }


    private void showSavedOptions() {
        progressBar = (ProgressBar) returnView.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        getSavedOptions();
    }

    private void getSavedOptions() {
        new GetCustprofile(getActivity(), listener).execute();
    }

    protected void createTransaction(OptionDetails optionDetails) {

        final String token = optionDetails.getToken();
        final String type = optionDetails.getType();

        final String txnId = "MRTXN" + String.valueOf(System.currentTimeMillis());

        JSONTaskComplete taskComplete = new JSONTaskComplete() {
            @Override
            public void onTaskExecuted(JSONObject[] paymentObject, String signature) {

                if (TextUtils.equals(type, "NETBANKING")) {
                    fillDetails(txnId, signature, token, "");
                }
                else {
                    processCardFlow(txnId, signature, token);
                }
            }
        };

        GetSign sign = new GetSign(getActivity(), txnId, JSONUtils.TXN_AMOUNT, taskComplete);
        sign.execute();

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
            JSONObject amount = JSONUtils.fillinAmountDetails();

            JSONObject address = JSONUtils.fillinAddress();

            JSONObject userDetails = JSONUtils.fillinUserDetails(getActivity(), address);

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
        taskExecuted = new JSONTaskComplete() {

            @Override
            public void onTaskExecuted(JSONObject[] paymentObject, String message) {
                if (TextUtils.equals(message, "success")) {
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

    private void showPopup() {
        final String email = OneClicksignup.getDefaultGmail(getActivity());

        final BooleanTask ontask = new BooleanTask() {
            @Override
            public void ontaskComplete(boolean result) {
                  if (result) {
                      Toast.makeText(getActivity().getApplicationContext(), "An Email has been sent to you", Toast.LENGTH_SHORT).show();
                  }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Reset Password?");

        builder.setMessage("An email will be sent to " + email);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 new ResetPassword(getActivity(), email, ontask).execute();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create();

        builder.show();
    }
}