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

package com.citrus.sdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.citrus.sdk.Constants;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.interfaces.Messenger;
import com.citrus.sdk.webops.ActivateAc;
import com.citrus.sdk.webops.SignUpAsynch;
import com.citruspay.mobile.payment.OnTaskCompleted;

import org.json.JSONObject;

/**
 * Created by shardul on 27/5/14.
 */
public class SignUp extends Activity {
    private OnTaskCompleted getProfileListener, signInListener;

    private ProgressBar spinner;

    private RelativeLayout signUpLayout;

    private EditText usernameET, passwordET, mobileET;

    private TextView errorText;

    private Button signUpButton;

    private OnTaskCompleted listener;

    private Messenger accListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initViews();
        initListeners();
    }

    private void initViews() {
        usernameET = (EditText) this.findViewById(R.id.username);
        passwordET = (EditText) this.findViewById(R.id.password);
        mobileET = (EditText) this.findViewById(R.id.mobile);
        errorText = (TextView) this.findViewById(R.id.errorText);
        signUpLayout = (RelativeLayout) this.findViewById(R.id.signUpLayout);
        signUpButton = (Button) this.findViewById(R.id.signUp);
        spinner = (ProgressBar) this.findViewById(R.id.progressBar1);
        signUpLayout.setVisibility(View.VISIBLE);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeScreen.logoutUser(SignUp.this);
                String mobile = mobileET.getText().toString();
                String password = passwordET.getText().toString();
                String username = usernameET.getText().toString();
                if (isValidInput(username, password, mobile)) {
                    String params[] = new String[] {randomEmail(), mobile, password, "", ""};
                    signUpLayout.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    new SignUpAsynch(SignUp.this, listener).execute(params);
                }
                else {
                    return;
                }

            }
        });
    }

    private void initListeners() {
        listener = new OnTaskCompleted() {
            @Override
            public void onTaskExecuted(JSONObject[] paymentObject, String message) {

                if (TextUtils.equals(message, "success")) {
                    activateAccount();

                }
                else if (TextUtils.equals(Constants.USER_EXISTS, message)) {
                    signUpLayout.setVisibility(View.VISIBLE);
                    errorText.setText("User already exists.");
                    HomeScreen.logoutUser(SignUp.this);
                }
                else {
                    signUpLayout.setVisibility(View.VISIBLE);
                    errorText.setText("Could not sign up the user");
                }
            }
        };

    }

    private void activateAccount() {
        accListener = new Messenger() {
            @Override
            public void onTaskExecuted(String result) {
                spinner.setVisibility(View.INVISIBLE);
                if (TextUtils.equals(result, "success")) {
                    Intent intent = new Intent(SignUp.this, MemberFlow.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    signUpLayout.setVisibility(View.VISIBLE);
                    errorText.setText("Could not sign up the user");
                }
            }
        };

        new ActivateAc(SignUp.this, accListener).execute();
    }

    /* This function only demonstrates the usage of one click sign up -
    *  Do not use this function in real application
    *  RandomEmail will create problem.
    * */
    private String randomEmail() {
        String email = String.valueOf(Math.random()) + "@gmail.com";
        return email;
    }

    private boolean isValidInput(String username, String password, String mobile) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(mobile)) {
            return false;
        }
        return true;
    }
}
