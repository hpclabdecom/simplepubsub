package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


//this server represents the producer in a producer/consumer strategy
//it receives a client socket and inserts it into a resource
public class Server {
    protected GenericConsumer<Socket> consumer;
    protected GenericResource<Socket> resource;
    protected int port;
    protected ServerSocket serverSocket;
    protected boolean isPrimary;
    protected String secondaryServer;
    protected int secondaryPort;

    public Server(int port) {
        this.port = port;
        isPrimary = true;
        secondaryServer = null;
        secondaryPort = -1;

        resource = new GenericResource<Socket>();
    }

    public Server(int port, boolean isPrimary, String secondaryServer, int secondaryPort) {
        this.port = port;
        this.isPrimary = isPrimary;
        this.secondaryServer = secondaryServer;
        this.secondaryPort = secondaryPort;

        resource = new GenericResource<Socket>();

    }


    public void begin() {
        try {

            //just one consumer to guarantee a single
            //log write mechanism
            consumer = new PubSubConsumer<Socket>(resource, isPrimary, secondaryServer, secondaryPort);

            consumer.start();

            openServerSocket();

            //start listening
            listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void listen() {


        while (!resource.isStopped()) {

            try {
                Socket clientSocket = this.serverSocket.accept();

                resource.putRegister(clientSocket);
            } catch (IOException e) {
                if (resource.isStopped()) {
                    //System.out.println("Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                        "Error accepting connection", e);
            }


        }
        System.out.println("Stopped: " + port);

    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Listening on port: " + this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + port, e);
        }
    }

    public void stop() {
        resource.stopServer();
        listen();

        consumer.stopConsumer();
        resource.setFinished();
        //consumer.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //System.out.println("verifying consumer.... " + consumer.isInterrupted());
    }

    public List<Message> getLogMessages() {
        try {
            return ((PubSubConsumer<Socket>) consumer).getMessages();
        } catch (Exception e) {
            return null;
        }
    }

}
