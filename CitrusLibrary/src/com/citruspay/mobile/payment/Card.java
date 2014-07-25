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

package com.citruspay.mobile.payment;

import com.citruspay.mobile.payment.internals.DateUtils;
import com.citruspay.mobile.payment.internals.TextUtils;

public class Card {
	private final String cardnumber;
	private String cardCVV;
	private String nameOnCard;
	private String cardType;
	private Integer expMonth;
	private Integer expYear;
	private String paymentMode;

	public Card(String cardNum, String expDate, String cvv, String name) {
		this.cardnumber = TextUtils.nullIfBlank(normalizeCardNumber(cardNum));
		this.cardCVV = TextUtils.nullIfBlank(cvv);
		this.nameOnCard = TextUtils.nullIfBlank(name);
		splitDate(expDate);
	}
			
	/***
    /* The below methods are to retrieve cardDetails 
    */
    
	public String getCardType() {
		String type = null;
		if (TextUtils.isBlank(cardType) && !TextUtils.isBlank(cardnumber)) {
            type = CardType.typeOf(cardnumber).toString();
			return type;
		}
		type = cardType;
		return type;
	}
	
	public String getDisplayCardNumber() {
		if (cardnumber != null && cardnumber.length() > 4) {
            return cardnumber.substring(cardnumber.length() - 4, cardnumber.length());
        }
        return null;
	}
	
	public String getCardNumber() {
		return this.cardnumber;
	}
	
	public String getCardHolderName() {
		return this.nameOnCard;
	}
	
	public String getPaymentMode() {
		return this.paymentMode;
	}
	
	public String getExpiryYear() {
		return ("20" + String.valueOf(expYear));
	}
	
	public String getExpiryMonth() {

        if (expMonth < 10) {
            return "0" + String.valueOf(expMonth);
        }

        return String.valueOf(expMonth);
	}
	
	public String getCvvNumber() {
		return this.cardCVV;
	}
	
	
	private void splitDate(String expDate) {
		if (!expDate.contains("/")) {
			expMonth = null;
			expYear = null;
		}
		else {
			expMonth = Integer.valueOf(expDate.split("/")[0]);
			expYear = Integer.valueOf(expDate.split("/")[1]);
		}
	}
		
	private String normalizeCardNumber(String number) {
	      if (number == null) {
	        return null;
	      }
	      return number.trim().replaceAll("\\s+|-", "");
    }
	
	public boolean validateCard() {
        if (cardCVV == null) {
            return validateNumber() && validateExpiryDate();
        } else {
            return validateNumber() && validateExpiryDate() && validateCVC();
        }
    }

    public boolean validateNumber() {
        cardType = getCardType();
        if (TextUtils.isBlank(cardnumber)) {
            return false;
        }

        String rawNumber = cardnumber.trim().replaceAll("\\s+|-", "");
        if (TextUtils.isBlank(rawNumber)
                || !TextUtils.isWholePositiveNumber(rawNumber)
                || !isValidLuhnNumber(rawNumber)) {
            return false;
        }

        if (!"AMEX".equals(cardType) && rawNumber.length() != 16) {
        	return false;
        }

        if ("AMEX".equals(cardType) && rawNumber.length() != 15) {
        	return false;
        }

        return true;
    }

    public boolean validateExpiryDate() {
    	if (!validateExpMonth()) {
    		return false;
    	}
    	if (!validateExpYear()) {
    		return false;
    	}
    	return !DateUtils.hasMonthPassed(expYear, expMonth);
    }

    public boolean validateExpMonth() {
    	if (expMonth == null) {
    		return false;
    	}
    	return (expMonth >= 1 && expMonth <= 12);
    }

    public boolean validateExpYear() {
    	if (expYear == null) {
    		return false;
    	}
    	return !DateUtils.hasYearPassed(expYear);
    }

    public boolean validateCVC() {
        if (TextUtils.isBlank(cardCVV)) {
            return false;
        }
        String cvcValue = cardCVV.trim();
        cardType = getCardType();
        boolean validLength = ((cardType == null && cvcValue.length() >= 3 && cvcValue.length() <= 4) ||
                ("AMEX".equals(cardType) && cvcValue.length() == 4) ||
                (!"AMEX".equals(cardType) && cvcValue.length() == 3));


        if (!TextUtils.isWholePositiveNumber(cvcValue) || !validLength) {
            return false;
        }
        return true;
    }

    private boolean isValidLuhnNumber(String number) {
        boolean isOdd = true;
        int sum = 0;

        for (int index = number.length() - 1; index >= 0; index--) {
            char c = number.charAt(index);
            if (!Character.isDigit(c)) {
                return false;
            }
            int digitInteger = Integer.parseInt("" + c);
            isOdd = !isOdd;

            if (isOdd) {
                digitInteger *= 2;
            }

            if (digitInteger > 9) {
                digitInteger -= 9;
            }

            sum += digitInteger;
        }

        return sum % 10 == 0;
    }
    
}
