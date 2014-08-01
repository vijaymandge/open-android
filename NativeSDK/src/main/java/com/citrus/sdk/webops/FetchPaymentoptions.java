package com.citrus.sdk.webops;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.OpenService;
import com.citruspay.mobile.payment.JSONTaskComplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by shardul on 11/7/14.
 */
public class FetchPaymentoptions extends AsyncTask<Void, Void, Void> {

    private ProgressDialog dialog;

    private Activity activity;

    private String result;

    private JSONObject paymentObject;

    private JSONTaskComplete taskCompleted;

    public FetchPaymentoptions(Activity activity, JSONTaskComplete taskCompleted) {
        this.activity = activity;

        this.taskCompleted = taskCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(activity, "Please Wait", "Fetching Bank Options", true, true);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MobileClient client = new MobileClient(activity, Constants.CITRUS_OAUTH_URL);

        OpenService service = client.getOpenService(Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET);

        try {
            paymentObject =  service.getPaymentOptions(Constants.VANITY_URL);

            result = "success";
        } catch (IOException e) {

        } catch (JSONException e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        taskCompleted.onTaskExecuted(new JSONObject[]{paymentObject}, result);
    }
}
