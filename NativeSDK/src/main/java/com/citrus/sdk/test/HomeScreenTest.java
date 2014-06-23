package com.citrus.sdk.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.citrus.sdk.activity.Guestflow;
import com.citrus.sdk.activity.HomeScreen;
import com.citrus.sdk.activity.MemberFlow;
import com.citrus.sdk.activity.SignIn;
import com.citrus.sdk.activity.SignUp;
import com.citrus.sdk.demo.R;
import com.robotium.solo.Solo;

/**
 * Created by shardul on 5/6/14.
 */
public class HomeScreenTest extends ActivityInstrumentationTestCase2<HomeScreen> {
    private Activity activity;
    private Button logout, signUp, signIn, member, guest;
    private Solo solo;
    public HomeScreenTest() {
        super(HomeScreen.class);
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

    public void testHomeScreen() throws Exception {
        solo.assertCurrentActivity("HomePageTest", HomeScreen.class);
    }

    public void testGuestButton() throws Exception {
        solo.clickOnButton("Guest Flow");
        solo.assertCurrentActivity("GuestFlow Activity", Guestflow.class);
    }

    public void testMemberButton() throws Exception {
        solo.clickOnButton("Member Flow");
        solo.assertCurrentActivity("MemberFlow Activity", MemberFlow.class);
    }

    public void testSignInButton() throws Exception {
        solo.clickOnButton("Sign In");
        solo.assertCurrentActivity("Sign In Activity", SignIn.class);
    }

    public void testSignUpButton() throws Exception {
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Sign Up Activity", SignUp.class);
    }

    public void testLogoutButton() throws Exception {
        solo.clickOnButton("Log out");
    }
}
