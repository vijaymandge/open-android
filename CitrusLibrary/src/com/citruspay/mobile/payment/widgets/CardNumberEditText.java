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

package com.citruspay.mobile.payment.widgets;

import java.util.regex.Pattern;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.citrus.citruslibrary.R;
import com.citruspay.mobile.payment.CardPatterns;
import com.citruspay.mobile.payment.CardType;
import com.citruspay.mobile.payment.internals.TextUtils;

public class CardNumberEditText extends EditText {
	
	public static final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
	public static final Pattern EXP_PATTERN = Pattern.compile("^((0[1-9])|(1[0-2]))//((2009)|(20[1-2][0-9]))$");
	
	private TextWatcher customTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String testString="";
			if (s.toString().contains(" ")){
				testString = s.toString().replace(" ", "");
			}
			else {
				testString = s.toString();
			}

			if (TextUtils.hasAnyPrefix(testString, CardPatterns.AMEX)) {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(18)});
				setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.amex), null);
				setError(null);
			}
			else if (TextUtils.hasAnyPrefix(testString, CardPatterns.DISCOVER)) {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(19)});
				setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.discover), null);
				setError(null);
			}
			else if (TextUtils.hasAnyPrefix(testString, CardPatterns.JCB)) {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(19)});
				setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.jcb), null);
				setError(null);
			}
			else if (TextUtils.hasAnyPrefix(testString, CardPatterns.DINERCLUB)) {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(19)});
				setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.diner), null);
				setError(null);
			}
			
			else if (TextUtils.hasAnyPrefix(testString, CardPatterns.VISA)) {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(19)});
				setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.visa), null);
				setError(null);
			}
			else if (TextUtils.hasAnyPrefix(testString, CardPatterns.MAESTRO)) {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(19)});
				setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.maestro), null);
				setError(null);
			}
			else if (TextUtils.hasAnyPrefix(testString, CardPatterns.MASTERCARD)) {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(19)});
				setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.master), null);
				setError(null);
			}
			else {
				setFilters(new InputFilter[] { new InputFilter.LengthFilter(19)});				
				setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (s.length() > 0 && !CODE_PATTERN.matcher(s).matches()) {
                String input = s.toString();
                String numbersOnly = keepNumbersOnly(input);
                String code = formatNumbersAsCode(numbersOnly);
                removeTextChangedListener(this);
                setText(code);
                setSelection(code.length());
                addTextChangedListener(this);
            }
		}
	};
	
	public CardNumberEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public CardNumberEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public CardNumberEditText(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		addTextChangedListener(customTextWatcher);		
	}

	private String keepNumbersOnly(CharSequence s) {
        return s.toString().replaceAll("[^0-9]", ""); 
	}
	
	private String formatNumbersAsCode(CharSequence s) {        
		int groupDigits = 0;
        String tmp = "";
        for (int i = 0; i < s.length(); ++i) {
            tmp += s.charAt(i);
            ++groupDigits;
            if (groupDigits == 4) {
                tmp += " ";
                groupDigits = 0;
            }
        }
        try {
        	if (tmp.charAt(tmp.length() - 1) == ' ') {
            	tmp = tmp.substring(0, tmp.length() - 1);
            }
        } catch (Exception e) {
        	tmp = "";
        }
        
        
        return tmp;
    }
		
}
