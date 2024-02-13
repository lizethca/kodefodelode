package chatApp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
		//two way communication between user + client
    public static void main(String[] args) throws Exception {
        Socket clientSoc = new Socket("localhost", 40002);
        OutputStream os = clientSoc.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = clientSoc.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        
        //user authentication + account creation
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();
        dos.writeUTF(username);
        dos.writeUTF(password);
        
        //authentication status printed
        String authStatus = dis.readUTF();
        if (authStatus.equals("authenticated")) {
            System.out.println("User authenticated");
            for (int i = 0; i < 3; i++) {
                String receivedMessage = dis.readUTF();
                System.out.println("Server: " + receivedMessage);
                System.out.print("Client: ");
                String messageToSend = scanner.nextLine();
                dos.writeUTF(messageToSend);
            }
        } else {
            System.out.println("Authentication failed.");
        }
        
        clientSoc.close();
        dos.close();
        dis.close();
    }
}
