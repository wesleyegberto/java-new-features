import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.Predicate;

/**
 * To run from the terminal we can use the utility command-line tool:
 * `jwebserver -b 0.0.0.0 -p 8080`
 *
 * The API only supports GET and HEAD requests, but we can added manual support with `HttpHandlers.handleOrElse`.
 *
 * Doc: https://download.java.net/java/early_access/jdk18/docs/api/jdk.httpserver/com/sun/net/httpserver/SimpleFileServer.html
 */
public class SimpleWebServer {
	private static final String SERVER_DIR = System.getProperty("user.dir");

	public static void main(String[] args) {
		var server = SimpleFileServer.createFileServer(
			new InetSocketAddress(8080),
			Path.of(SERVER_DIR),
			SimpleFileServer.OutputLevel.VERBOSE
		);

		var defaultHandler = HttpHandlers.of(404, new Headers(), "Not found");

		Predicate<Request> IS_POST = req -> "POST".equals(req.getRequestMethod());

		var postHandler = HttpHandlers.handleOrElse(
			IS_POST,
			(exchange) -> {
				System.out.println("Handling POST request");

				System.out.println("Headers:");
				exchange.getRequestHeaders().forEach((k, v) -> System.out.printf("\t%s: %s\n", k, v));

				InputStream requestBody = exchange.getRequestBody();
				try (var reader = new BufferedReader(new InputStreamReader(requestBody))) {
					System.out.println("\nBody:");
					reader.lines().forEach(line -> System.out.printf("\t%s", line));
				} catch (RuntimeException | IOException ex) {
					System.err.println("Error reading request body: " + ex.getMessage());
				}

				var responseBody = "POST handled successfully".getBytes();
				exchange.sendResponseHeaders(200, responseBody.length);
				exchange.getResponseBody().write(responseBody);
			},
			defaultHandler
		);
		server.createContext("/post", postHandler);

		server.start();
		System.out.println("Server started at " + server.getAddress());
	}
}
