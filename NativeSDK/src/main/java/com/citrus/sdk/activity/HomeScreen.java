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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.citrus.sdk.Constants;
import com.citrus.sdk.ImageHelper;
import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.operations.JSONUtils;
import com.citrus.sdk.operations.OneClicksignup;
import com.citrus.sdk.webops.SavecontactDetails;
import com.citruspay.mobile.client.Logout;
import com.citruspay.mobile.client.openservice.User;
import com.citruspay.mobile.payment.BooleanTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HomeScreen extends Activity {
    private Button paybutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logoutuser:
                logoutUser(HomeScreen.this);

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initButton() {
        paybutton = (Button) this.findViewById(R.id.paybutton);

        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkifMember(OneClicksignup.getDefaultGmail(HomeScreen.this));
            }
        });

        ImageView buyer = (ImageView) this.findViewById(R.id.imageLogo);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.buyer);
        buyer.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 75));

    }


    private void checkifMember(String email) {
        User user = new User(HomeScreen.this, Constants.SIGNIN_ID, Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET, Constants.CITRUS_OAUTH_URL, new BooleanTask() {
            @Override
            public void ontaskComplete(boolean result) {
                    if (result) {
                        Intent intent = new Intent(HomeScreen.this, MemberFlow.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(HomeScreen.this, Guestflow.class);
                        startActivity(intent);
                    }
            }
        });

        user.isMember(email);
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
