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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.citrus.sdk.Constants;
import com.citrus.sdk.demo.R;
import com.citrus.sdk.fragments.CreditCard;
import com.citrus.sdk.fragments.DebitCard;
import com.citrus.sdk.fragments.Netbanking;
import com.viewpagerindicator.TabPageIndicator;

public class Guestflow extends FragmentActivity {
    private static final String[] OPTIONS = new String[] {"Net Banking", "Debit Card", "Credit Card"};
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guest_flow);
        bundle = new Bundle();
        FragmentPagerAdapter adapter = new GuestPageAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    public class GuestPageAdapter extends FragmentPagerAdapter {
        public GuestPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            bundle.putString(Constants.PAY_TYPE, Constants.GUEST_FLOW);
            if (position == 0) {
                Netbanking netbanking = new Netbanking();
                netbanking.setArguments(bundle);
                return netbanking;
            }
            else if (position == 1) {
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
