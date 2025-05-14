package net.javaguides.ems_backend.websocket;

import org.java_websocket.client.WebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.net.URI;
import java.net.URISyntaxException;

// @SpringBootApplication
@Component ("WSServer")
public class WebSocketProxyApplication {

    private ProxyWebSocketServer server;

    @PostConstruct
    public void startWebSocketServer() {
        System.out.println("The server");
        int port = 8081; // Change this port if needed
        server = new ProxyWebSocketServer(port);

        try {
            server.start();
            System.out.println("Proxy WebSocket server is up on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

