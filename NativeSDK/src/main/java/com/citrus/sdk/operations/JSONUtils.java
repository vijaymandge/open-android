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

import com.citruspay.mobile.payment.Card;
import com.citruspay.mobile.payment.internals.PaymentUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shardul on 23/6/14.
 * Following static functions use dummy values.
 * Merchants need to either provide these parameters in run time or fetch them from shared preferences.
 * All the parameters are mandatory
 * DO NOT STORE CARD PAYMENT DETAILS - FETCH THEM FROM USER IN RUNTIME
 */
public class JSONUtils {

    public static final String TXN_AMOUNT = "1";

    /*Make sure that none of the fields below are blank or null - else, there would be errors.*/

    /*Following are the payment details for member flow*/

    public static final JSONObject fillinAddress() {
        JSONObject address = new JSONObject();
        try {
            address.put("street1", "streetone");
            address.put("street2", "streetwo");
            address.put("city", "Mumbai");
            address.put("state", "Maharashtra");
            address.put("country", "India");
            address.put("zip", "400056");
        } catch (JSONException e) {

        }
        return address;
    }

    public static final JSONObject fillinUserDetails(JSONObject address) {
        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("email", "tester@gmail.com");
            userDetails.put("firstName", "Tester");
            userDetails.put("lastName", "Citrus");
            userDetails.put("mobileNo", "1234567890");
            userDetails.put("address", address);
        } catch (JSONException e) {

        }

        return userDetails;
    }

    public static final JSONObject fillinAmountDetails() {
        JSONObject amountDetails = new JSONObject();
        try {
            amountDetails.put("currency", "INR");
            amountDetails.put("value", TXN_AMOUNT);
        } catch (JSONException e) {

        }
        return amountDetails;
    }

    public static final JSONObject fillinPaymentToken(JSONObject paymentMode) {
        JSONObject paymentToken = new JSONObject();
        try {
            paymentToken.put("type", "paymentOptionToken");
            paymentToken.put("paymentMode", paymentMode);
        } catch (JSONException e) {

        }

        return paymentToken;
    }

    /*Following are the payment details for Guest flow*/

    public static final JSONObject fillinGuestPayCardDetails(String type, Card card) {
        JSONObject paymentDetails = new JSONObject();
        try {
            paymentDetails.put("paymentMode", type);
            paymentDetails.put("cardType", card.getCardType());
            paymentDetails.put("cardNumber", card.getCardNumber());
            paymentDetails.put("cardHolderName", card.getCardHolderName());
            paymentDetails.put("expiryMonth", card.getExpiryMonth());
            paymentDetails.put("expiryYear", card.getExpiryYear());
            paymentDetails.put("cvvNumber", card.getCvvNumber());

        } catch (JSONException e) {

        }
        return paymentDetails;
    }

    public static final JSONObject fillinGuestPayNetDetails(String bankcode) {
        JSONObject paymentDetails = new JSONObject();
        try {
            paymentDetails.put("paymentMode", PaymentUtils.NET_BANKING.toString());
            paymentDetails.put("issuerCode", bankcode);
        } catch (JSONException e) {

        }
        return paymentDetails;
    }


    public static final JSONObject fillinGuestPersonalDetails() {
        JSONObject personalDetails = new JSONObject();
        try {
            personalDetails.put("firstName", "Tester");
            personalDetails.put("lastName", "Citrus");
            personalDetails.put("address", "Test Address");
            personalDetails.put("addressCity", "Mumbai");
            personalDetails.put("addressState", "Maharashtra");
            personalDetails.put("addressZip", "885744");
            personalDetails.put("email", "monishbaba@gmail.com");
            personalDetails.put("mobile", "1234567890");
        } catch (JSONException e) {

        }
        return personalDetails;
    }

    public static final JSONObject fillinContactDetails() {
        JSONObject contact = new JSONObject();
        try {
            contact.put("firstName", "Tester");
            contact.put("lastName", "Citrus");
            contact.put("email", "monishbaba@gmail.com");
            contact.put("mobile", "1234567890");
        } catch (JSONException e) {

        }
        return contact;
    }

}
