package chatApp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
    private static final Map<String, String> userCredentials = new HashMap<>();

    static {
        //Pre-defined user credentials for midterm
        userCredentials.put("user1", "password1");
        userCredentials.put("user2", "password2");
    }

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(40002);
        System.out.println("Welcome to Bow. Start your conversation");
        Socket serverSoc = listener.accept();
        InputStream is = serverSoc.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        OutputStream os = serverSoc.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        //authenticating user
        boolean authenticated = authenticateUser(dis.readUTF(), dis.readUTF());
        if (authenticated) {
            System.out.println("User authenticated.");
            for (int i = 0; i < 3; i++) {
                System.out.println(dis.readUTF());
                Scanner scan = new Scanner(System.in);
                String msg = scan.nextLine();
                dos.writeUTF(msg);
            }
        } else {
            System.out.println("Authentication failed.");
        }
    }
    private static boolean authenticateUser(String username, String password) {
        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}


