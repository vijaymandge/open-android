package com.citrus.sdk.webops;

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.SubscriptionException;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.payment.BooleanTask;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

/**
 * Created by shardul on 9/7/14.
 */
public class ResetPassword extends AsyncTask<Void, Void, Boolean> {
    private String email;

    private MobileClient mobileClient;

    private SubscriptionService subscriptionService;

    private Activity activity;

    private BooleanTask task;

    public ResetPassword(Activity activity, String email, BooleanTask task) {
        this.activity = activity;
        this.email = email;
        this.task = task;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean result;
        mobileClient = new MobileClient(activity, Constants.CITRUS_OAUTH_URL);

        subscriptionService = mobileClient.getSubscriptionService(Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET, Constants.SIGNIN_ID, Constants.SIGNIN_SECRET);

        try {
            subscriptionService.resetPassword(email);
            result = true;
        } catch (OAuth2Exception e) {
            result = false;
        } catch (SubscriptionException e) {
            result = false;
        }


        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        this.task.ontaskComplete(aBoolean);
    }
}
