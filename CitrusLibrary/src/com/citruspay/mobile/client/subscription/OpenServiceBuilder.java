package com.citruspay.mobile.client.subscription;

import com.citruspay.mobile.payment.client.rest.RESTClient;
import com.citruspay.mobile.payment.oauth2.OAuth2ClientId;
import com.citruspay.mobile.payment.oauth2.OAuth2GrantType;
import com.citruspay.mobile.payment.oauth2.OAuth2Service;
import com.citruspay.mobile.payment.oauth2.OAuth2TokenStore;
import com.citruspay.mobile.payment.oauth2.SimpleOAuth2Service;

import org.apache.http.client.HttpClient;

/**
 * Created by shardul on 11/7/14.
 */
public class OpenServiceBuilder {

    private OAuth2ClientId signup;
    private OAuth2TokenStore tokens ;
    private RESTClient restOauth, restOpen;
    private HttpClient http;


    public OpenService build() {
        OAuth2Service up = new SimpleOAuth2Service(restOauth, signup, tokens);
        return new OpenService(restOpen, up);
    }

    public OpenServiceBuilder tokens(OAuth2TokenStore store) {
        tokens = store;
        return this;
    }

    public OpenServiceBuilder oauth(RESTClient restOauth ) {
        this.restOauth = restOauth;
        return this;
    }

    public OpenServiceBuilder openserv(RESTClient restopen) {
        this.restOpen = restopen;
        return this;
    }


    public OpenServiceBuilder signup(String clientId,
                                             String clientSecret) {
        signup = new OAuth2ClientId(clientId, clientSecret,
                OAuth2GrantType.implicit);
        return this;
    }


    public OpenServiceBuilder http(HttpClient http) {
        this.http = http;
        return this;
    }

}

