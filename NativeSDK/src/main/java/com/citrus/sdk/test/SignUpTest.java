package com.citrus.sdk.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.citrus.sdk.activity.SignIn;
import com.citrus.sdk.activity.SignUp;
import com.citrus.sdk.demo.R;
import com.robotium.solo.Solo;

/**
 * Created by shardul on 23/6/14.
 */
public class SignUpTest extends ActivityInstrumentationTestCase2<SignUp>{
    private Activity activity;
    private Solo solo;

    public SignUpTest() {
        super(SignUp.class);
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

    public void testSignUpInputs() {
        EditText username = (EditText) activity.findViewById(R.id.username);
        EditText mobile = (EditText) activity.findViewById(R.id.mobile);
        EditText password = (EditText) activity.findViewById(R.id.password);
        solo.enterText(username, "");
        solo.enterText(password, "");
        solo.enterText(mobile, "");
        solo.clickOnButton("Sign Up");
        assertEquals(false, false);

        solo.enterText(username, "abcd@gmail.com");
        solo.enterText(password, "as#123");
        solo.enterText(mobile, "1234567890");
        solo.clickOnButton("Sign Up");
        assertEquals(true, true);
    }
}
