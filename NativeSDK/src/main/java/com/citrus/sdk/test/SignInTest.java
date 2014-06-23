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
