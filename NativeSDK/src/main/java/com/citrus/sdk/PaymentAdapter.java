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


package com.citrus.sdk;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.interfaces.Messenger;
import com.citrus.sdk.operations.SetDefault;
import com.citruspay.mobile.client.subscription.OptionDetails;

import java.util.List;

public class PaymentAdapter extends BaseAdapter {
	public List<OptionDetails> payList;
	private Activity mActivity;
	
	public PaymentAdapter(Activity mActivity) {
		this.mActivity = mActivity;
		this.payList = getPayList();
	}
	
	private List<OptionDetails> getPayList() {
		DBHandler db = new DBHandler(mActivity);
		List<OptionDetails> payList = db.getSavedOptions();
		return payList;
	}

	@Override
	public int getCount() {
		return payList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return payList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			arg1 = inflater.inflate(R.layout.payoption, null);
		}
		
		TextView type = (TextView) arg1.findViewById(R.id.optionType);
		TextView option = (TextView) arg1.findViewById(R.id.paymentDetail);

		ImageButton defaultOption = (ImageButton) arg1.findViewById(R.id.defaultImage);

        if (payList.get(arg0).getDefaultStatus() == 1) {
            TextView defaultText = (TextView) arg1.findViewById(R.id.defaultText);
            defaultText.setVisibility(View.VISIBLE);
        }
		
		defaultOption.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                updateDefaultOption(payList.get(arg0));
			}
		});
		
		type.setText(payList.get(arg0).getType());
		option.setText(payList.get(arg0).getName());
		
		
		return arg1;
	}

    public void updateDefaultOption(final OptionDetails option) {
        Messenger messenger = new Messenger() {
            @Override
            public void onTaskExecuted(String result) {
                if (TextUtils.isEmpty(result)) {
                    DBHandler dbHandler = new DBHandler(mActivity.getApplicationContext());
                    dbHandler.setDefaultOption("");
                    dbHandler.setDefaultOption(option.getName());
                    payList = getPayList();
                    notifyDataSetChanged();
                }
            }
        };

        if (option.getDefaultStatus() == 1) {
            Toast.makeText(mActivity.getApplicationContext(), "This is already a default option", Toast.LENGTH_SHORT).show();
            return;
        }



        SetDefault defaultOp = new SetDefault(mActivity);
        defaultOp.setdefaultOption(option, messenger);

    }

}
