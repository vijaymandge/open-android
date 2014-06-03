/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.citrus.sdk.operations;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.citrus.sdk.interfaces.Messenger;
import com.citrus.sdk.webops.Web3DSecure;
import com.citruspay.mobile.payment.OnTaskCompleted;
import com.citruspay.mobile.payment.internals.PaymentUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shardul on 27/5/14.
 */
public class GuestCheckout {
    private Activity activity;
    private Messenger messenger;

    public GuestCheckout(Activity activity) {
        this.activity = activity;

    }

    public void cardPay(String type) {
        messenger = new Messenger() {
            @Override
            public void onTaskExecuted(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Intent intent = new Intent(activity, Web3DSecure.class);
                    intent.putExtra("redirectUrl", result);
                    activity.startActivity(intent);
                }
                else {
                    Toast.makeText(activity.getApplicationContext(), "Could not process your request", Toast.LENGTH_SHORT).show();
                }
            }
        };
        String txnId = String.valueOf(System.currentTimeMillis());
        JSONObject paymentObject = new JSONObject();
        try {
            paymentObject.put("paymentMode", type);
            paymentObject.put("cardType", "VISA");
            paymentObject.put("cardNumber", "4028530052708001");
            paymentObject.put("cardHolderName", "Jitendra Gupta");
            paymentObject.put("expiryMonth", "03");
            paymentObject.put("expiryYear", "2015");
            paymentObject.put("cvvNumber", "018");

            paymentObject.put("merchantTxnId", txnId);

			/*Personal Details*/
            paymentObject.put("amount", "1.0");
            paymentObject.put("firstName", "Shardul");
            paymentObject.put("lastName", "Swwww");
            paymentObject.put("address", "Baner");
            paymentObject.put("addressCity", "Pune");
            paymentObject.put("addressState", "Goa");
            paymentObject.put("addressZip", "885744");
            paymentObject.put("email", "shardullavekar@gmail.com");
            paymentObject.put("mobile", "7875432990");

            paymentObject.put("returnUrl", "http://103.13.97.20/citrus/index.php");

            new com.citrus.sdk.webops.GuestPay(activity, paymentObject, messenger, txnId).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void netbankPay(String bankCode) {
        messenger = new Messenger() {
            @Override
            public void onTaskExecuted(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Intent intent = new Intent(activity, Web3DSecure.class);
                    intent.putExtra("redirectUrl", result);
                    activity.startActivity(intent);
                }
                else {
                    Toast.makeText(activity.getApplicationContext(), "Could not process your request", Toast.LENGTH_SHORT).show();
                }
            }
        };
        String txnId = String.valueOf(System.currentTimeMillis());
        JSONObject paymentObject = new JSONObject();
        try {
            paymentObject.put("paymentMode", PaymentUtils.NET_BANKING.toString());
            paymentObject.put("merchantTxnId", txnId);

			/*Personal Details*/
            paymentObject.put("amount", "1.0");
            paymentObject.put("firstName", "Shardul");
            paymentObject.put("lastName", "Swwww");
            paymentObject.put("address", "Baner");
            paymentObject.put("addressCity", "Pune");
            paymentObject.put("addressState", "Goa");
            paymentObject.put("addressZip", "885744");
            paymentObject.put("email", "shardullavekar@gmail.com");
            paymentObject.put("mobile", "7875432990");
            paymentObject.put("issuerCode", bankCode);
            paymentObject.put("returnUrl", "http://103.13.97.20/citrus/index.php");

        } catch (JSONException e) {

        }
        new com.citrus.sdk.webops.GuestPay(activity, paymentObject, messenger, txnId).execute();
    }
}
