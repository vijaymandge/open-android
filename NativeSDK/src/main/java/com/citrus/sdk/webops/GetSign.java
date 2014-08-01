package com.citrus.sdk.webops;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.citrus.sdk.Constants;
import com.citruspay.mobile.payment.JSONTaskComplete;
import com.citruspay.mobile.payment.client.rest.ProtocolException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shardul on 28/7/14.
 */
public class GetSign extends AsyncTask<Void, Void, String> {
    private Activity activity;

    private String txnId, amount;

    private JSONTaskComplete taskComplete;

    private ProgressDialog waitBox;
    public GetSign(Activity activity, String txnid, String amount, JSONTaskComplete taskComplete) {
        this.activity = activity;

        this.txnId = txnid;

        this.amount = amount;

        this.taskComplete = taskComplete;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        waitBox = ProgressDialog.show(activity, "Please Wait", "Connecting..", true, true);
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpResponse response = null;

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        pairs.add(new BasicNameValuePair("transactionId", txnId));

        pairs.add(new BasicNameValuePair("amount", amount));

        HttpPost post = new HttpPost(URI.create(Constants.SIGNATURE_URL));

        HttpClient httpClient = new DefaultHttpClient();

        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
        } catch (UnsupportedEncodingException uex) {
            throw new ProtocolException(uex);
        }

        try {
            response = httpClient.execute(post);
        } catch (IOException e) {

        }


        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        waitBox.dismiss();
        taskComplete.onTaskExecuted(new JSONObject[]{}, s);
    }
}
