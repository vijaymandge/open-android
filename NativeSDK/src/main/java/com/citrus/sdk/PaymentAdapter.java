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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.database.OptionDetails;
import com.citrus.sdk.demo.R;

import java.util.List;

public class PaymentAdapter extends BaseAdapter {
	public List<OptionDetails> payList;
	private Context context;
	
	public PaymentAdapter(Context context) {
		this.context = context;
		this.payList = getPayList();
	}
	
	private List<OptionDetails> getPayList() {
		DBHandler db = new DBHandler(context);
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			arg1 = inflater.inflate(R.layout.payoption, null);
		}
		
		TextView type = (TextView) arg1.findViewById(R.id.optionType);
		TextView option = (TextView) arg1.findViewById(R.id.paymentDetail);
		
		ImageButton delete = (ImageButton) arg1.findViewById(R.id.deleteButton);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Toast.makeText(context, "Delete Button", Toast.LENGTH_SHORT).show();
			}
		});
		
		ImageButton defaultOption = (ImageButton) arg1.findViewById(R.id.defaultImage);
		
		defaultOption.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Toast.makeText(context, "Default Button", Toast.LENGTH_SHORT).show();
			}
		});
		
		type.setText(payList.get(arg0).getType());
		option.setText(payList.get(arg0).getName());
		
		
		return arg1;
	}

}
