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


package com.citruspay.mobile.payment.client.rest;

public class Optional<T> {
	private final T value;
	
	private Optional(T value) {
		this.value = value;
	}
	
	public boolean isPresent() {
		return (value != null);
	}
	
	public T get() {
		if (!isPresent()) {
			throw new IllegalStateException();
		}
		return value;
	}
	
	public static <T> Optional<T> of(T value) {
		return new Optional<T>(value);
	}
	
	public static <T> Optional<T> absent() {
		return new Optional<T>(null);
	}
}
