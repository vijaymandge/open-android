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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citrus.sdk.Constants;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.operations.SMSParsing;
import com.citrus.sdk.webops.JSInterface;


public class Web3DSecure extends Activity {
	private android.webkit.WebView webView;

    private TextView otpText;

    private RelativeLayout otpLayout;

    private Button submitButton;

    private WebViewClient webViewClient;

    private String url, otpValue;

    private BroadcastReceiver broadcastReceiver;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web3_dsecure);

        initReceiver();

        registerReceiver(broadcastReceiver, new IntentFilter(Constants.SMS_RECEIVED));

		Intent intent = getIntent();

        url = intent.getStringExtra("redirectUrl");

        webView = (android.webkit.WebView) this.findViewById(R.id.loadCardWebView);

        otpLayout = (RelativeLayout) this.findViewById(R.id.otpLayout);

        submitButton = (Button) this.findViewById(R.id.submit);

        otpText = (TextView) this.findViewById(R.id.otpView);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webSettings.setBuiltInZoomControls(false);

        webView.addJavascriptInterface(new JSInterface(Web3DSecure.this), "CitrusResponse");

        initButton();

        initWebViewClient();

        webView.loadUrl(url);

	}

    private void initButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterOTP();
            }
        });
    }

    private void initReceiver() {
        otpValue = "";
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getExtras().getString("message");
                otpValue = SMSParsing.extractOTP(message);

                if (!android.text.TextUtils.isEmpty(otpValue)) {
                    otpLayout.setVisibility(View.VISIBLE);
                    otpText.setText("OTP is " + otpValue);
                }
            }
        };
    }

	private void initWebViewClient() {
		webViewClient = new WebViewClient() {
			 private int       webViewPreviousState;
		     private final int PAGE_STARTED    = 0x1;
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

    private void enterOTP() {
        if (!TextUtils.isEmpty(otpValue)) {
            String javascript= "javascript: document.frmPayerAuth.txtOtp.value='"+otpValue+"';document.getElementById('frmPayerAuth').submit();";
            webView.loadUrl(javascript);
            otpLayout.setVisibility(View.INVISIBLE);
            otpValue = null;
        }
    }

}
