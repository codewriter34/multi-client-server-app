
import java.net.*;
import java.io.*;

public class Client {
    //initiliaze socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    private String clientId;

    //constructor to put ip address, port and client id
    public Client(String address, int port, String clientId) {
        this.clientId = clientId;
        //establish connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            //takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            out = new DataOutputStream(
                socket.getOutputStream());
        
        }
        catch (UnknownHostException u){
            System.out.println(u);
            return;
        }
        catch (IOException i){
            System.out.println(i);
            return;
        }

        //send client id to the server
        try {
            out.writeUTF(clientId);
        }
        catch (IOException i){
            System.out.println(i);
        }

        //string to read message from input
        String line = "";

        //keep reading until null "Over" is input
        while(!line.equals("Over")){
            try {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch (IOException i){
                System.out.println(i);
            }
        }

        //close the connection
        try {
            input.close();
            out.close();
            socket.close();
        }
        catch (IOException i){
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Client client1 = new Client("127.0.0.1", 5000, "bright");
        
    }
}