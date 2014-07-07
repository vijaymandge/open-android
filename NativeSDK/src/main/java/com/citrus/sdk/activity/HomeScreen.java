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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.operations.JSONUtils;
import com.citrus.sdk.webops.SavecontactDetails;
import com.citruspay.mobile.client.Logout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HomeScreen extends Activity {
    private Button guestFlow, memberFlow, signIn, signUp, contact, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initButton();
    }

    private void initButton() {
        guestFlow = (Button) this.findViewById(R.id.guestFlow);
        memberFlow = (Button) this.findViewById(R.id.memberFlow);
        signIn = (Button) this.findViewById(R.id.signIn);
        signUp = (Button) this.findViewById(R.id.signUp);
        logout = (Button) this.findViewById(R.id.logout);
        contact = (Button) this.findViewById(R.id.updateContact);

        guestFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Guestflow.class);
                startActivity(intent);
            }
        });

        memberFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, MemberFlow.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, SignIn.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, SignUp.class);
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContact();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser(HomeScreen.this);
            }
        });
    }

    private void updateContact() {
        JSONObject contactobject = new JSONObject();

        JSONObject contactDetails = JSONUtils.fillinContactDetails();

        Iterator<String> iterator = contactDetails.keys();

        try {
            contactobject.put("type", "contact");

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                contactobject.put(key, contactDetails.getString(key));
            }

        } catch (JSONException e) {

        }

        new SavecontactDetails(this, contactobject).execute();
    }

    public static final void logoutUser(Activity activity) {
        try {
            Logout.logoutUser(activity);
            DBHandler.deleteDB(activity);
            Toast.makeText(activity.getApplicationContext(), "User Logged out successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "Could not logout successfully!", Toast.LENGTH_SHORT).show();
        }

    }

}
