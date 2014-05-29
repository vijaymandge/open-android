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

package com.citrus.sdk.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.citrus.sdk.Constants;
import com.citrus.sdk.ImageHelper;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.fragments.CreditCard;
import com.citrus.sdk.fragments.DebitCard;
import com.citrus.sdk.fragments.Netbanking;
import com.citrus.sdk.fragments.SavedOptions;
import com.viewpagerindicator.TabPageIndicator;

public class MemberFlow extends FragmentActivity {
    private static final String[] OPTIONS = new String[] {"Saved Options", "Net Banking", "Debit Card", "Credit Card"};
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pay_options);
        ImageView buyer = (ImageView) this.findViewById(R.id.imageLogo);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.buyer);
        buyer.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 75));
        bundle = new Bundle();
        FragmentPagerAdapter adapter = new MemberAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    public class MemberAdapter extends FragmentPagerAdapter {
        public MemberAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            bundle.putString(Constants.PAY_TYPE, Constants.MEMBER_FLOW);
            if (position == 0) {
                SavedOptions savedOptions = new SavedOptions();
                savedOptions.setArguments(bundle);
                return savedOptions;
            }
            else if (position == 1) {
                Netbanking netbanking = new Netbanking();
                netbanking.setArguments(bundle);
                return netbanking;
            }
            else if (position == 2) {
                DebitCard debitCard = new DebitCard();
                debitCard.setArguments(bundle);
                return debitCard;
            }
            else {
                CreditCard creditCard = new CreditCard();
                creditCard.setArguments(bundle);
                return creditCard;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return OPTIONS[position];
        }

        @Override
        public int getCount() {
            return OPTIONS.length;
        }
    }
}