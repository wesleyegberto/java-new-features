package com.github.wesleyegberto.collections.reactive;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.IntStream;

public class FlowTest {
	public static void main(String[] args) {
		try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
			publisher.subscribe(new MySubscriber());

			IntStream.range(0, 15)
				.mapToObj(String::valueOf)
				.forEach(publisher::submit);
		}
	}
}

class MySubscriber implements Subscriber<String> {
	@Override
	public void onComplete() {
		System.out.println("Completed");
	}

	@Override
	public void onError(Throwable err) {
		System.out.println("Error: " + err.getMessage());
	}

	@Override
	public void onNext(String item) {
		System.out.println("Next: " + item);
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		System.out.println("Subscribed, requesting 10 items...");
		subscription.request(10);
	}

}