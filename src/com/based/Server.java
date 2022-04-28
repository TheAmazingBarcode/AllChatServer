package com.based;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ServerSocket server;
    private static ArrayList<ServerThread> connections = new ArrayList<>();
    private static int counter = 0;

    public Server(int portNumber) throws IOException {
        server = new ServerSocket(portNumber);
    }

    public void startServer() throws IOException {
        while(true) {
            System.out.println("[SERVER]: CURRENT CLIENTS  CONNECTED");
        connections.forEach(serverThread -> System.out.println(serverThread.getClient().getRemoteSocketAddress()));
        Socket client = server.accept();
        System.out.println("[SERVER]: CLIENT CONNECTED >> "+client.getInetAddress().getHostAddress());
        connections.add(new ServerThread(client,counter));
        connections.get(counter).start();
        counter++;
        }
    }

    public static ServerSocket getServer() {
        return server;
    }

    public static ArrayList<ServerThread> getConnections() {
        return connections;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Server.counter = counter;
    }
}
