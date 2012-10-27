package org.jug.jce.example;

public class Utils {

	public static String toHex(byte[] data) {
		String digits = "0123456789abcdef";
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i != data.length; i++) {
			int v = data[i] & 0xff;
			buf.append(digits.charAt(v >> 4));
			buf.append(digits.charAt(v & 0xf));
		}

		return buf.toString();
	}
}
