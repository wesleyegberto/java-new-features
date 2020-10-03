import java.net.URI;
import java.net.http.HttpRequest;

public class HttpBinRequestBuilder {
	public static HttpRequest createGetRequest() {
		// HttpRequest is immutable
		return HttpRequest.newBuilder()
				.GET()
				.uri(URI.create("http://httpbin.org/get"))
				.header("Accept-Language", "en-US,en;q=0.5")
				.build();
	}
}
