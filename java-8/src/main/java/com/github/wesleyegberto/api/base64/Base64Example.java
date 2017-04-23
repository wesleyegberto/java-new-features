package com.github.wesleyegberto.api.base64;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Wesley Egberto
 */
public class Base64Example {
	public static void main(String[] args) {
		final String text = "Base64 finally in Java 8!";
		final String encoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
		System.out.println(encoded);
		final String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
		System.out.println(decoded);
	}
}
