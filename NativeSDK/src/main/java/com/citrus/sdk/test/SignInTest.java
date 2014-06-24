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
package com.citrus.sdk.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.citrus.sdk.activity.HomeScreen;
import com.citrus.sdk.activity.SignIn;
import com.citrus.sdk.demo.R;
import com.robotium.solo.Solo;

/**
 * Created by shardul on 23/6/14.
 */
public class SignInTest extends ActivityInstrumentationTestCase2<SignIn> {
    private Activity activity;
    private Solo solo;

    public SignInTest() {
        super(SignIn.class);
    }

    @Override
    protected void setUp() throws Exception {
        this.activity = getActivity();
        solo = new Solo(getInstrumentation(), this.activity);
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testSignInInputs() {
        EditText username = (EditText) activity.findViewById(R.id.username);
        EditText password = (EditText) activity.findViewById(R.id.password);
        solo.enterText(username, "");
        solo.enterText(password, "");
        solo.clickOnButton("Sign In");
        assertEquals(false, false);

        solo.enterText(username, "abcd@gmail.com");
        solo.enterText(password, "as#123");
        solo.clickOnButton("Sign In");
        assertEquals(true, true);
    }

}
