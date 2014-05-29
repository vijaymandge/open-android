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



package com.citruspay.mobile.client.subscription;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A pseudo-random alphabetical password generator using the subscriber's email
 * as seed.
 */
public class PseudoRandomPasswordGenerator implements PasswordGenerator {

	private MessageDigest md5;

	/**
	 * Creates a new pseudo random password generator.
	 */
	public PseudoRandomPasswordGenerator() {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsax) {
			throw new RuntimeException(nsax);
		}
	}

	/**
	 * Generates a 8 character ([a-zA-Z]{8}) pseudo random password using the
	 * email as a seed.
	 * 
	 * As a consequence, same email will result in same password. Mobile number
	 * is ignored and can be null.
	 */
	@Override
	public String generate(String email, String mobile) {
		PseudoRandomNumberGenerator ran = new PseudoRandomNumberGenerator(
				generateSeed(email));
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			builder.append(ran.nextLetter());
		}
		return builder.toString();
	}

	/**
	 * Generates a non-random positive number from a string.
	 * 
	 * @param data
	 *            the string to generate seed from.
	 * @return the value of the 3 highest bytes of the MD5 sum of data.
	 */
	public int generateSeed(String data) {
		byte hash[] = md5.digest(data.getBytes());
		md5.reset();
		hash = copyOfRange(hash, hash.length - 3, hash.length);
		return new BigInteger(1, hash).intValue();
	}

	public static byte[] copyOfRange(byte[] original, int from, int to) {
		int newLength = to - from;
		if (newLength < 0)
			throw new IllegalArgumentException(from + " > " + to);
		byte[] copy = new byte[newLength];
		System.arraycopy(original, from, copy, 0,
				Math.min(original.length - from, newLength));
		return copy;

	}

	/**
	 * A pseudo random integer generator.
	 */
	public static class PseudoRandomNumberGenerator {
		private int state;

		/**
		 * Creates a new pseudo random number generator.
		 * 
		 * @param seed
		 *            the seed to start the generation from.
		 */
		public PseudoRandomNumberGenerator(int seed) {
			this.state = seed;
		}

		/**
		 * Generates the next pseudo-random integer. The number is positive in
		 * the [0-max[ interval.
		 * 
		 * @param max
		 *            the exclusive upper boundary of the interval to generate
		 *            the pseudo-random in.
		 * @return the next number in the pseudo-random suite modulo max.
		 */
		public int nextInt(int max) {
			state = 7 * state % 3001;
			return (state - 1) % max;
		}

		/**
		 * Generates the next pseudo-random alphabetical character. The letter
		 * is in the [a-zA-Z] range.
		 * 
		 * @return the next letter in the pseudo-random suite.
		 */
		public char nextLetter() {
			int n = nextInt(52);
			return (char) (n + ((n < 26) ? 'A' : 'a' - 26));
		}

	}
}
