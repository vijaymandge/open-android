package com.citruspay.mobile.client.openservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.citrus.mobile.client.load.PrepaidException;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.OpenService;
import com.citruspay.mobile.payment.BooleanTask;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

import org.json.JSONException;

/**
 * Created by shardul on 11/7/14.
 */
public class User {
    private String subId, subSecret, server_url, identifier, result;

    private Activity activity;

    private BooleanTask task;

    private MobileClient client;

    private OpenService service;

    public User(Activity activity, String subId, String subSecret, String server_url, BooleanTask ontask) {
        this.subId = subId;
        this.subSecret = subSecret;
        this.activity = activity;
        this.server_url = server_url;
        this.task = ontask;
    }

    public void isMember(String identifier) {
        initClient();
        if (!client.isSignedIn("")) {
            this.identifier = identifier;
            new Servercheckuser().execute();
        }

        else {
            task.ontaskComplete(true);
        }

    }

    private void initClient() {
        client = new MobileClient(activity, server_url);
    }

    private class Servercheckuser extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(activity, "Please Wait", "Checking user", true, true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            client = new MobileClient(activity, server_url);

            service = client.getOpenService(subId, subSecret);

            try {
                result = service.checkCitrusmember(identifier);
            } catch (OAuth2Exception e) {
                result = "oauthexception";

            } catch (JSONException e) {
                result = "jsonexception";

            } catch (PrepaidException e) {
                result = "prepaidexception";

            } catch (ProtocolException e) {
                result = "protoexception";

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (TextUtils.equals(result, "true")) {
                task.ontaskComplete(true);
            }
            else {
                task.ontaskComplete(false);
            }
        }
    }
}