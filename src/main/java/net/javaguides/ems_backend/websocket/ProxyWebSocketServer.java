package net.javaguides.ems_backend.websocket;

// package com.example.websocketproxy;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProxyWebSocketServer extends WebSocketServer {

    private final Set<WebSocket> clients = Collections.synchronizedSet(new HashSet<>());
    private WebSocket trustedClient = null;

    public ProxyWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        conn.send("Connected to proxy WebSocket server. Send 'REGISTER_TRUSTED' to become trusted.");
        System.out.println("New client connected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        String password = "REGISTER_TRUSTED";

//        if (message.equals(password)) {
//            System.out.println("password match");
//        } else {
//            System.out.println("msg != pw");
//        }
        System.out.println(trustedClient);
        // Register the trusted client
         if ("REGISTER_TRUSTED".equals(message) && trustedClient == null) {
//        if ((password == message)) {
            trustedClient = conn;
            conn.send("You are now registered as the trusted client.");
            System.out.println("Trusted client registered: " + conn.getRemoteSocketAddress());
            return;
        }

        // Only the trusted client is allowed to broadcast
        if (conn.equals(trustedClient)) {
            broadcastToOthers(conn, message);
        } else {
            conn.send("You are not authorized to broadcast.");
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        if (conn.equals(trustedClient)) {
            trustedClient = null;
            System.out.println("Trusted client disconnected.");
        } else {
            System.out.println("Client disconnected: " + conn.getRemoteSocketAddress());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("WebSocket error:");
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket proxy server started on port " + getPort());
    }

    private void broadcastToOthers(WebSocket sender, String message) {
        for (WebSocket client : clients) {
            if (!client.equals(sender)) {
                client.send(message);
            }
        }
    }
}

