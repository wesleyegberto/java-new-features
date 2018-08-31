package com.github.wesleyegberto.collections.httpclient;

import java.io.IOException;
import java.net.URI;
import jdk.incubator.http.*;

public class HttpClientTest {
	public static void main(String[] args) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("http://httpbin.org/uuid"))
			.GET()
			.build();
		
		String syncResponse = HttpClient.newHttpClient()
			.send(request, HttpResponse.BodyHandler.asString())
			.body();
		
		System.out.println("Sync response: " + syncResponse);
		
		HttpClient.newHttpClient()
		.sendAsync(request, HttpResponse.BodyHandler.asString())
		.thenApply(HttpResponse::body)
		.thenAccept(response -> System.out.println("Async response: " + response));
		

			/*
		    .responseAsync() // CompletableFuture
		    .thenAccept(httpResponse ->
		        System.out.println(httpResponse.body(HttpResponse.asString()))
		    );*/
	  Thread.sleep(5000);
	}
}
