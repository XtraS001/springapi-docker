package net.javaguides.ems_backend.websocket;

import jakarta.annotation.PostConstruct;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@DependsOn("WSServer")
public class WebSocketBridgeClient {

    private WebSocketClient serverAClient;
    private WebSocketClient serverBClient;

//    @PostConstruct
    public void start() {
        try {
            // 1. Connect to Server B first (receiver)
            serverBClient = new WebSocketClient(new URI("ws://localhost:8081")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("✅ Connected to Server B");
                    serverBClient.send("REGISTER_TRUSTED");

                }

                @Override
                public void onMessage(String message) {
                    System.out.println("📩 Received from B (optional): " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("🔌 Server B disconnected: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("❌ Error from B: " + ex.getMessage());
                }
            };
//            serverBClient.connectBlocking(); // Wait until connected
            System.out.println("🔄 Connecting to Server B...");
            boolean connected = serverBClient.connectBlocking(); // Wait here until connected

            if (!connected) {
                System.err.println("❌ Failed to connect to Server B. Aborting bridge setup.");
                return;
            }

//             2. Connect to Server A (upstream)
            serverAClient = new WebSocketClient(new URI("wss://ws.finnhub.io?token=d00j0n9r01qk939odf7gd00j0n9r01qk939odf80")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("✅ Connected to Server A aka FInnhub");
                    // send("{ \"type\": \"subscribe\", \"channel\": \"example\" }");
                    send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}");
                }

                @Override
                public void onMessage(String message) {
//                    System.out.println("📨 Received from A: " + message);

                    // Forward message to Server B
                    if (serverBClient != null && serverBClient.isOpen()) {
                        serverBClient.send(message);
                        System.out.println("➡️ Forwarded to Server B");
                    } else {
                        System.err.println("⚠️ Server B not connected, cannot forward.");
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("🔌 Server A disconnected: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("❌ Error from A: " + ex.getMessage());
                }
            };
            serverAClient.connect();

        } catch (Exception e) {
            System.err.println("❌ Failed to start WebSocket bridge: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

