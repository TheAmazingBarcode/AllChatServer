package com.based;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread{

    private Socket client;
    private volatile boolean shutDown = false;

    public ServerThread(Socket socket,int threadNum) {
        client = socket;
        this.setName(String.valueOf(threadNum));
    }

    public void run() {
        DataInputStream incoming = null;

        try {
            incoming = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(shutDown == false) {
            try {
                while (true) {
                    String message = incoming.readUTF();

                    if (message.equals("CLOSING CONNECTION")) {
                        int counter = 0;
                        for (ServerThread connection : Server.getConnections()) {
                            if (connection.getName().equals(this.getName())) {
                                Server.getConnections().remove(counter);
                                shutDownServer();
                                Server.setCounter(Server.getCounter()-1);
                                break;
                            }
                            counter++;
                        }
                    }


                    String messageArgs[] = message.split("_");
                    if (messageArgs.length == 4) {
                        transportMessage(messageArgs[1], messageArgs[2], messageArgs[3]);
                    }

                    System.out.println(message);
                }


            } catch (SocketException e) {
                shutDownServer();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void shutDownServer() {
        shutDown = true;
    }

    public void transportMessage(String content,String address,String userTo) throws IOException {
        for (ServerThread client : Server.getConnections()) {
            if(client.getClient().getRemoteSocketAddress().toString().contains(address)) {
                DataOutputStream output = new DataOutputStream(client.getClient().getOutputStream());
                output.writeUTF(content+"_"+userTo);
                output.flush();
                System.out.println("Sent out message");
            }

        }
    }

    public Socket getClient() {
        return client;
    }
}
