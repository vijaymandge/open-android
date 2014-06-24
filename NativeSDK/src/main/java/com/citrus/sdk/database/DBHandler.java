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

package com.citrus.sdk.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.subscription.OptionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper{
	
	public static final String DB_NAME = "USER_DETAILS";
	
	private static final int DB_VERSION = 1;
	
	private static final String PAYOPTION_TABLE = "PAY_OPTIONS";

    private static final String BANK_TABLE = "BANK_TABLE";
	
	private static final String MMID = "mmid";
	
	private static final String SCHEME = "scheme";
	
	private static final String EXPDATE = "expiryDate";
	
	private static final String NAME = "name";
	
	private static final String OWNER = "owner";
	
	private static final String BANK = "bank";
	
	private static final String NUMBER = "number";
	
	private static final String TYPE = "type";
	
	private static final String IMPS_REGISTERED = "impsRegisteredMobile";
	
	private static final String DEFAULT_OPTION = "defaultOption";
	
	private static final String TOKEN_ID = "token";

    private static final String BANK_CID = "bank_cid";
	
	
	public DBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String payTable = "CREATE TABLE IF NOT EXISTS " + PAYOPTION_TABLE + " (" + MMID + " TEXT, " + SCHEME + " TEXT, " 
				+ EXPDATE + " TEXT, " + NAME + " TEXT PRIMARY KEY, " + OWNER + " TEXT, " + BANK + " TEXT, " + NUMBER 
				+ " TEXT, " + TYPE + " TEXT, " + TOKEN_ID + " TEXT, " + IMPS_REGISTERED + " TEXT, " + DEFAULT_OPTION + " INTEGER)";
		String bankTable = "CREATE TABLE IF NOT EXISTS " + BANK_TABLE + " (" + BANK_CID + " TEXT PRIMARY KEY, " + BANK + " TEXT)";
        db.execSQL(payTable);
        db.execSQL(bankTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PAYOPTION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + BANK_TABLE);
        onCreate(db);
	}
	
	public void addPayOption(JSONArray paymentArray) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < paymentArray.length(); i++) {
			ContentValues loadValue = new ContentValues();
			try {
				JSONObject payOption = paymentArray.getJSONObject(i);
				loadValue.put(MMID, payOption.getString(MMID));
				loadValue.put(SCHEME, payOption.getString(SCHEME));
				loadValue.put(EXPDATE, payOption.getString(EXPDATE));
				loadValue.put(NAME, payOption.getString(NAME));
				loadValue.put(OWNER, payOption.getString(OWNER));
				loadValue.put(BANK, payOption.getString(BANK));
				loadValue.put(NUMBER, payOption.getString(NUMBER));
				loadValue.put(TYPE, payOption.getString(TYPE).toUpperCase());
				loadValue.put(IMPS_REGISTERED, payOption.getString(IMPS_REGISTERED));
				loadValue.put(TOKEN_ID, payOption.getString(TOKEN_ID));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			db.insert(PAYOPTION_TABLE, null, loadValue);
				
		}
		
	}

  	public int setDefaultOption(String optionName) {
        String whereValues[] = new String[]{optionName};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();

        if (!TextUtils.isEmpty(optionName)) {
            try {
                newValues.put(DEFAULT_OPTION, 1);
                return db.update(PAYOPTION_TABLE, newValues, NAME + "=?", whereValues);
            } catch (Exception e) {
                db.close();
                return 0;
            }

        }
        else {
            try {
                newValues.put(DEFAULT_OPTION, 0);
                return db.update(PAYOPTION_TABLE, newValues, null, null);
            } catch (Exception e) {
                db.close();
                return 0;
            }

        }
	}
	
	public List<OptionDetails> getSavedOptions() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor current_cursor;
		String[] columNames = {MMID, SCHEME ,EXPDATE, NAME, OWNER, BANK, NUMBER, TYPE, IMPS_REGISTERED, TOKEN_ID, DEFAULT_OPTION};
		String orderBy = DEFAULT_OPTION + " DESC";
		List<OptionDetails> savedOptions = new ArrayList<OptionDetails>();
		current_cursor = db.query(PAYOPTION_TABLE, columNames, null, null, null, null, orderBy);
		if (current_cursor.moveToFirst()) {
			do {
				OptionDetails currentOption = new OptionDetails(current_cursor);
				savedOptions.add(currentOption);
			} while (current_cursor.moveToNext());
		}
		return savedOptions;
	}

    public void addBankOptions(JSONArray netbankingOption) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < netbankingOption.length(); i++) {
            ContentValues loadValue = new ContentValues();
            try {
                JSONObject bankOption = netbankingOption.getJSONObject(i);
                loadValue.put(BANK, bankOption.getString("bankName"));
                loadValue.put(BANK_CID, bankOption.getString("issuerCode"));
            } catch (JSONException e) {

            }
            db.insert(BANK_TABLE, null, loadValue);
        }
    }

    public List<BankOptions> getBankOptions() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor current_cursor;
        String[] columNames = {BANK_CID, BANK};
        String orderBy = DEFAULT_OPTION + " DESC";
        List<BankOptions> bankOptionsList = new ArrayList<BankOptions>();
        current_cursor = db.query(PAYOPTION_TABLE, columNames, null, null, null, null, orderBy);
        if (current_cursor.moveToFirst()) {
            do {
                BankOptions currentOption = new BankOptions(current_cursor.getString(0), current_cursor.getString(1));
                bankOptionsList.add(currentOption);
            } while (current_cursor.moveToNext());
        }
        return bankOptionsList;
    }

    public static boolean deleteDB(Activity activity) {
        return activity.getApplicationContext().deleteDatabase(DB_NAME);
    }

}
