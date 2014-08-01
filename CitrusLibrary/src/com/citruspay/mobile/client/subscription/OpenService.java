package com.citruspay.mobile.client.subscription;

import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.client.rest.RESTException;
import com.citruspay.mobile.payment.oauth2.AuthorizationUtil;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;
import com.citruspay.mobile.payment.oauth2.OAuth2Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shardul on 11/7/14.
 */
public class OpenService {

    private RESTClient rest;
    private OAuth2Service signup;

    public OpenService(RESTClient rest, OAuth2Service signUp) {
        this.rest = rest;
        this.signup = signUp;
    }

    public String checkCitrusmember(String identifier) throws JSONException, ProtocolException,
            OAuth2Exception {
        Map<String, String> params = new HashMap<String, String>();

        String result = null;

        params.put("email", identifier);

        try {
          result = rest.openPost(URI.create("verify/email"), AuthorizationUtil.asHeader(signup.getAuthorization()), params);
        } catch (RESTException e) {
            handleRESTException(e);
        }

        return result;
    }

    public JSONObject getPaymentOptions(String vanity) throws IOException, JSONException {
        Map<String, String> params = new HashMap<String, String>();

        JSONObject paymentOptions = null;

        params.put("vanity", vanity);

        try {
            paymentOptions = rest.post(URI.create("merchant/pgsetting"), null, params);
        } catch (RESTException e) {
            handleRESTException(e);
        }

        return paymentOptions;
    }



    private JSONObject handleRESTException(RESTException rx)
            throws ProtocolException {

        throw new ProtocolException(rx);

    }
}
