import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.*;
import java.nio.*;
import java.nio.file.*;
import java.rmi.server.SocketSecurityException;

public class UnixDomainSocketTest {
	private static final String SOCKET_PATH = "server.socket";

	public static void main(String[] args) throws Exception {
		var socketFile = Path.of(System.getProperty("user.home")).resolve(SOCKET_PATH);
		var address = UnixDomainSocketAddress.of(socketFile);

		Thread.sleep(500);
		new Thread(new ServerProcess(address)).start();

		Thread.sleep(1000);
		new Thread(new ClientProcess(address)).start();

		Thread.currentThread().join(10_000);
	}
}

class ServerProcess implements Runnable {
	private final UnixDomainSocketAddress address;

	ServerProcess(UnixDomainSocketAddress address) {
		this.address = address;
	}

	public void run() {
		try (ServerSocketChannel server = ServerSocketChannel.open(StandardProtocolFamily.UNIX)) {
			server.bind(this.address);

			System.out.println("[Server] starting");
			SocketChannel channel = server.accept();

			System.out.println("[Server] sending message");
			channel.write(ByteBuffer.wrap("Hello client!".getBytes()));

			var buffer = ByteBuffer.allocate(1024);
			var length = channel.read(buffer);
			if (length > 0) {
				System.out.println("[Server] message from client: " + new String(buffer.array()));
			} else {
				System.out.println("[Server] empty");
			}
			channel.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class ClientProcess implements Runnable {
	private final UnixDomainSocketAddress address;

	ClientProcess(UnixDomainSocketAddress address) {
		this.address = address;
	}

	public void run() {
		try (SocketChannel client = SocketChannel.open(StandardProtocolFamily.UNIX)) {
			System.out.println("[Client] starting");
			client.connect(this.address);

			System.out.println("[Client] reading message");
			var buffer = ByteBuffer.allocate(1024);
			var length = client.read(buffer);
			if (length > 0) {
				System.out.println("[Client] message from the server: " + new String(buffer.array()));
			} else {
				System.out.println("[Client] empty");
			}
			System.out.println("[Client] sending message");
			client.write(ByteBuffer.wrap("Hello server, I'm your new client!".getBytes()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
