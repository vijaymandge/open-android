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
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.demo.R;
import com.citrus.sdk.webops.GetCustprofile;
import com.citrus.sdk.webops.SignInAsynch;
import com.citruspay.mobile.payment.OnTaskCompleted;

import org.json.JSONObject;

/**
 * Created by shardul on 27/5/14.
 */
public class SignIn extends Activity {
    private OnTaskCompleted getProfileListener, signInListener;

    private ProgressBar spinner;

    private RelativeLayout signInLayout;

    private EditText usernameET, passwordET;

    private TextView errorText;

    private Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_options);
        spinner = (ProgressBar) this.findViewById(R.id.progressBar1);
        getProfileListener = new OnTaskCompleted() {
            @Override
            public void onTaskExecuted(JSONObject[] paymentObject, String message) {
                spinner.setVisibility(View.INVISIBLE);
                if (TextUtils.equals(message, "oauth")) {
                    Toast.makeText(getApplicationContext(), "User is not signed in!", Toast.LENGTH_SHORT).show();
                    showSignInFlow("User not signed in");
                }
                else {
                    showMemberFlow();
                }
            }
        };

        signInListener = new OnTaskCompleted() {

            @Override
            public void onTaskExecuted(JSONObject[] paymentObject, String message) {
                spinner.setVisibility(View.INVISIBLE);
                if (TextUtils.equals(message, "signedIn")) {
                    showMemberFlow();
                }
                else {
                    showSignInFlow("User not signed in");
                }
            }
        };
        getprofile();
    }

    private void getprofile() {
        spinner.setVisibility(View.VISIBLE);
        new GetCustprofile(SignIn.this, getProfileListener).execute();
    }

    private void showSignInFlow(String message) {
        signInLayout = (RelativeLayout) this.findViewById(R.id.signInLayout);
        signInLayout.setVisibility(View.VISIBLE);
        usernameET = (EditText) this.findViewById(R.id.username);
        passwordET = (EditText) this.findViewById(R.id.password);
        errorText= (TextView) this.findViewById(R.id.errorText);
        errorText.setText(message);
        Button signIn = (Button) this.findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                String params[] = new String[]{username, password};
                signInLayout.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
                new SignInAsynch(SignIn.this, signInListener).execute(params);
            }
        });
    }

    private void showMemberFlow() {
        Intent intent = new Intent(SignIn.this, MemberFlow.class);
        startActivity(intent);
        this.finish();
    }
}
