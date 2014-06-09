package com.citrus.sdk.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.citrus.sdk.activity.HomeScreen;
import com.citrus.sdk.demo.R;

/**
 * Created by shardul on 5/6/14.
 */
public class ExampleTest extends ActivityInstrumentationTestCase2<HomeScreen> {
    private Activity activity;
    private Button logout, signUp, signIn, member, guest;

    public ExampleTest() {
        super(HomeScreen.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.activity = getActivity();
        setActivityInitialTouchMode(true);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        activity.finish();
    }

    public void testOnClicks() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logout = (Button)activity.findViewById(R.id.logout);
                logout.performClick();
                signUp = (Button)activity.findViewById(R.id.signUp);
                signUp.performClick();
                signIn = (Button)activity.findViewById(R.id.signIn);
                signIn.performClick();
                member = (Button)activity.findViewById(R.id.memberFlow);
                member.performClick();
                guest = (Button)activity.findViewById(R.id.guestFlow);
                guest.performClick();
            }
        });


    }


}
