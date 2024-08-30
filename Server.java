
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private Map<String, DataOutputStream> clients = new HashMap<>();

    // constructor with port
    public Server(int port){
        // starts server and waits for a connection
        try {

            server = new ServerSocket(port);
            System.out.println("Server Started");

            while(true) {
                socket = server.accept();
                System.out.println("Client accepted");

                // takes input from the client socket
                in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

                // read client id from the socket
                String clientId = in.readUTF();

                // create a new thread for the client
                Thread clientThread = new Thread() {
                    public void run() {
                        try {
                            // add client to the clients map
                            clients.put(clientId, new DataOutputStream(
                                socket.getOutputStream()));

                            // send welcome message to the client
                            clients.get(clientId).writeUTF("Welcome, " + clientId);

                            // read messages from the client and send to all clients
                            String line = "";
                            while (!line.equals("Over")) {
                                try {
                                    line = in.readUTF();
                                    System.out.println(clientId + " sent: " + line);
                                    for (DataOutputStream out : clients.values()) {
                                        out.writeUTF(clientId + " sent: " + line);
                                    }
                                } 
                                catch (IOException i) {
                                    System.out.println(clientId + " disconnected");
                                    clients.remove(clientId);
                                }
                            }
                            System.out.println("Closing connection");

                            // close connection
                            socket.close();
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                // start the new thread
                clientThread.start();

                // add the new thread to the list
                clients.put(clientId, null);
            }
        }
        catch (IOException i){
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Server server = new Server(5000);
    }
}



