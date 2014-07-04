package com.citruspay.mobile.payment.options;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.citruspay.mobile.payment.OnTaskCompleted;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shardul on 24/6/14.
 */
public class PaymentOptions {
    private static final String PAY_OPTION_URL = "https://sandboxadmin.citruspay.com/service/v1/merchant/pgsetting";

    private Activity activity;

    private String vanity, result;

    private JSONObject optionobject;

    private OnTaskCompleted listener;

    public PaymentOptions(Activity activity) {
       this.activity = activity;
    }

    public void getPaymentOptions(String vanity, OnTaskCompleted listener) {
        this.vanity = vanity;
        this.listener = listener;
        new AsynchOption().execute();
    }


    public class AsynchOption extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(activity, "Please Wait", "Fetching Bank Options", true, true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(PAY_OPTION_URL);
            HttpResponse response = null;

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("vanity", vanity));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                response = httpclient.execute(httppost);

                optionobject = new JSONObject(EntityUtils.toString(response.getEntity()));

                result = "success";

            } catch (ClientProtocolException e) {
                result = "protoException";

            } catch (IOException e) {
                result = "ioException";

            } catch (JSONException e) {
                result = "jsonException";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            listener.onTaskExecuted(new JSONObject[]{optionobject}, result);
        }
    }
}
