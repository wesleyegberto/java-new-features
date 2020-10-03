import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetRequestAsyncTest {
	public static void main(String[] args) throws InterruptedException {
		HttpRequest request = HttpBinRequestBuilder.createGetRequest();

		HttpClient httpClient = HttpClientBuilderTest.createHttpClient();
		// HttpClient#sendAsync is non-blocking and returns a CompletableFuture
		httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(body -> System.out.println("Async Response: " + body))
				.exceptionally(ex -> {
					System.out.println("Exception: " + ex.getLocalizedMessage());
					return null;
				})
				.join(); // let's wait
	}
}
