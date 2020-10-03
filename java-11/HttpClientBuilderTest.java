import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientBuilderTest {
    public static HttpClient createHttpClient() {
        // The client is immutable =)
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2) // default value
                .connectTimeout(Duration.ofSeconds(3))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }
}
