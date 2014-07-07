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

package com.citrus.sdk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.citrus.sdk.demo.R;
import com.citrus.sdk.webops.JSInterface;


public class Web3DSecure extends Activity {
    android.webkit.WebView webView;
    WebViewClient webViewClient;
    String url;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web3_dsecure);
        Intent intent = getIntent();
        url = intent.getStringExtra("redirectUrl");
        webView = (android.webkit.WebView) this.findViewById(R.id.loadCardWebView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webView.addJavascriptInterface(new JSInterface(Web3DSecure.this), "CitrusResponse");
        initWebViewClient();
        webView.loadUrl(url);

    }

    private void initWebViewClient() {
        webViewClient = new WebViewClient() {
            private int webViewPreviousState;
            private final int PAGE_STARTED = 0x1;
            private final int PAGE_REDIRECTED = 0x2;
            private ProgressDialog dialog;
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String urlNewString) {
                webViewPreviousState = PAGE_REDIRECTED;
                webView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewPreviousState = PAGE_STARTED;
                if (dialog == null || !dialog.isShowing())
                    dialog = ProgressDialog.show(Web3DSecure.this, "Please Wait", "Redirecting to Citrus", true, true,
                            new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {

                                }
                            });
            }

            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {

                if (webViewPreviousState == PAGE_STARTED && dialog !=null) {
                    dialog.dismiss();
                    dialog = null;
                }

            }


        };

        webView.setWebViewClient(webViewClient);
    }

}
