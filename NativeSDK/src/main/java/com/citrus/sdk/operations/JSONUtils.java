package com.citrus.sdk.operations;

import com.citruspay.mobile.payment.internals.PaymentUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shardul on 23/6/14.
 * Following static functions use dummy values - merchants need to build function according to their need.
 * All the parameters are mandatory
 *
 */
public class JSONUtils {

    public static final String TXN_AMOUNT = "1";


    /*Following are the payment details for member flow*/

    public static final JSONObject fillinAddress() {
        JSONObject address = new JSONObject();
        try {
            address.put("street1", "Test Street");
            address.put("street2", "Test Street 2");
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

    public static final JSONObject fillinGuestPayCardDetails(String type) {
        JSONObject paymentDetails = new JSONObject();
        try {
            paymentDetails.put("paymentMode", type);
            paymentDetails.put("cardType", "VISA");
            paymentDetails.put("cardNumber", "4028530052708001");
            paymentDetails.put("cardHolderName", "Jitendra Gupta");
            paymentDetails.put("expiryMonth", "01");
            paymentDetails.put("expiryYear", "2020");
            paymentDetails.put("cvvNumber", "000");
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
            personalDetails.put("address", "");
            personalDetails.put("addressCity", "Mumbai");
            personalDetails.put("addressState", "Maharashtra");
            personalDetails.put("addressZip", "885744");
            personalDetails.put("email", "tester@gmail.com");
            personalDetails.put("mobile", "1234567890");
        } catch (JSONException e) {

        }
        return personalDetails;
    }

}
