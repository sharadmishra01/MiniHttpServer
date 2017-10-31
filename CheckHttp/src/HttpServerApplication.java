import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class HttpServerApplication {

	public static void main(String args[]) throws IOException {
		int port = 9000;
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		System.out.println("server started at " + port);
		server.createContext("/", new RootHandler());
		server.createContext("/echoHeader",
				new DeviceTypeIdentificationHandler());
		// server.createContext("/echoGet", new EchoGetHandler());
		server.createContext("/deviceType",
				new DeviceTypeIdentificationHandler());
		server.setExecutor(null);
		server.start();
	}

}
