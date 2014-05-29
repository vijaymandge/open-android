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

package com.citrus.mobile.client.load;



import java.math.BigDecimal;

/**
 * The specification of a money amount.
 *
 * An amount is defined by a positive, 2 decimals value
 * and a string ISO-4217 currency code (USD, EUR, INR, etc.).
 */
public class Amount {
    private float value;
    private String currency;

    /**
     * Creates a new amount.
     * Values will be truncated to 2 decimals as
     * per {{@link #normalizeValue(float)}}.
     *
     * @param value the value of the new amount,
     * truncated as necessary.
     * @param currency the ISO-4217 code of the
     * currency of the new amount.
     * @exception IllegalArgumentException if the value
     * is negative.
     */
    public Amount(float value, String currency) {
        this.value = normalizeValue(value);
        this.currency = currency;
    }

    /**
     * Creates an empty amount.
     * Empty amount is zero value and empty currency code.
     */
    public Amount() {
        this(0.0f, "");
    }

    /**
     * Amount value getter.
     *
     * @return the value of the amount.
     */
    public float getValue() {
        return this.value;
    }

    /**
     * Amount value setter.
     * The value will be truncated to 2 decimals as
     * per {{@link #normalizeValue(float)}}.
     *
     * @param value the new value of the amount, truncated
     * to 2 decimals.
     * @exception IllegalArgumentException if the specified
     * value is negative.
     */
    public void setValue(float value) {
        this.value = normalizeValue(value);
    }

    /**
     * Currency getter.
     *
     * @return the ISO-4217 code of the currency for this
     * amount.
     */
    public String getCurrency() {
        return this.currency;
    }

    /**
     * Currency setter.
     * @param currency the new ISO-4217 currency code
     * for this amount.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Normalizes an amount value.
     * Truncates the value to 2 decimals:
     * <ul>
     *   <li>12.451 will be truncated to 12.45</li>
     *   <li>12.455 will be truncated to 12.45</li>
     *   <li>12.458 will be truncated to 12.45</li>
     * </ul>
     * and checks that the value is not negative.
     *
     * @param value the value to normalize.
     * @return the normalized value.
     * @throws IllegalArgumentException if the value is negative.
     */
    public static float normalizeValue(float value) {
        if (value < 0.0F) {
            throw new IllegalArgumentException("A negative value is not a valid amount");
        }
        return new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                .floatValue();
    }
}
