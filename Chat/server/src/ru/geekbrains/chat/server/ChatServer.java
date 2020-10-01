package ru.geekbrains.chat.server;

import ru.geekbrains.chat.network.TCPConnection;
import ru.geekbrains.chat.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    public static void main (String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while(true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                }  catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        }   catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnetionReady(ru.geekbrains.chat.network.TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(ru.geekbrains.chat.network.TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(ru.geekbrains.chat.network.TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnect: " + tcpConnection);
    }

    @Override
    public synchronized void onException(ru.geekbrains.chat.network.TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) connections.get(i).sendString(value);
    }
}
