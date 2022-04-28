package com.based;

import java.io.IOException;

public class Main  {

    public static void main(String[] args) throws IOException {
     Server server = new Server(7777);
     server.startServer();
    }

}
