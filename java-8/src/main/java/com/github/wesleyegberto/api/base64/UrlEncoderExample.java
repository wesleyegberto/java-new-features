package com.github.wesleyegberto.api.base64;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Wesley Egberto
 */
public class UrlEncoderExample {
	public static void main(String[] args) {
		String url = "http://github.com/wesleyegberto";
		String encodedUrl = Base64.getUrlEncoder().encodeToString(url.getBytes(StandardCharsets.UTF_8));
		String Decoded = new String(Base64.getUrlDecoder().decode(encodedUrl), StandardCharsets.UTF_8);
		System.out.println("encodedUrl = " + encodedUrl);
		System.out.println("decodedUrl = "+ Decoded);
	}
}
