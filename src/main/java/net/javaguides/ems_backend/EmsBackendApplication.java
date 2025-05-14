package net.javaguides.ems_backend;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.server.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.javaguides.ems_backend.websocket.EmptyClient;
import net.javaguides.ems_backend.websocket.SimpleServer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.server.WebSocketServer;


@SpringBootApplication
public class EmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsBackendApplication.class, args);

//		String host = "localhost";
//        int port = 8887;
//
//        WebSocketServer server = new SimpleServer(new InetSocketAddress(host, port));
//        server.run();
//
//        try {
//            WebSocketClient client = new EmptyClient(new URI("ws://localhost:8887"));
//            client.connect();
//        } catch (URISyntaxException e) {
//            System.err.println("Invalid URI: " + e.getMessage());
////            e.printStackTrace();
//        }


	}

}
