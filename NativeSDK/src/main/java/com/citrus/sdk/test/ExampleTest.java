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
    private Button logout;

    public ExampleTest() {
        super(HomeScreen.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        this.activity = getActivity();
        logout = (Button)this.activity.findViewById(R.id.logout);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


}
