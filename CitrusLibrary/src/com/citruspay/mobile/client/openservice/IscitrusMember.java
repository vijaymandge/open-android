package com.citruspay.mobile.client.openservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

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
 * Created by shardul on 7/7/14.
 */
public class IscitrusMember {

    private Activity activity;

    private String vanity, result, service_url;

    private JSONObject optionobject;

    private OnTaskCompleted listener;

    public IscitrusMember(Activity activity) {
        this.activity = activity;
    }

    public void ismember(String service_url, String email, OnTaskCompleted listener) {
        this.vanity = email;
        this.listener = listener;
        this.service_url = service_url;
        new AsynchOption().execute();
    }

    private class AsynchOption extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(activity, "Please Wait", "Fetching Bank Options", true, true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(service_url + "service/v1/subscription/verify");
            HttpResponse response = null;

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("email", vanity));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                response = httpclient.execute(httppost);

                optionobject = new JSONObject(EntityUtils.toString(response.getEntity()));

                result = EntityUtils.toString(response.getEntity());

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