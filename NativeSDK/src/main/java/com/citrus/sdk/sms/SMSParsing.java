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
package com.citrus.sdk.sms;

/**
 * Created by shardul on 30/6/14.
 */
public class SMSParsing {

    public static final String extractOTP(String sms) {
        String otpValue = "";
        String splitValues[] = sms.split("is");
        int i = 1;

        try {
            while (Character.isDigit(splitValues[1].charAt(i))) {
                otpValue = otpValue + String.valueOf(splitValues[1].charAt(i));
                i++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

        return otpValue;
    }

}
