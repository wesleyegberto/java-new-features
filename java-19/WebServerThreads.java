import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.Predicate;
import java.util.concurrent.Executors;

/**
 * Run: `java --enable-preview --source 19 WebServerThreads.java`
 */
public class WebServerThreads {
	public static void main(String[] args) throws IOException {
		var server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/", HttpHandlers.of(200, new Headers(), "Handled!"));

		// server.setExecutor(Executors.newCachedThreadPool());
		server.setExecutor(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory()));

		server.start();
		System.out.println("Server started at " + server.getAddress());
	}
}
