package dev.lieno;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static ArrayList<String> users = new ArrayList<>();
    private static HashMap<String, Integer> tickets = new HashMap<>();

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10); 

    public static void main(String[] args) throws IOException {
        System.out.println("Server started.");

        // Creation of server socket
        ServerSocket ss = new ServerSocket(3000);
        ss.setReuseAddress(true);
        System.out.println("Listening on 0.0.0.0:" + ss.getLocalPort());

        tickets.put("gold", 10);
        tickets.put("pit", 30);
        tickets.put("parterre", 60);
        
        
        while (true) {
            
            // Creation of socket
            Socket s = ss.accept();
            System.out.println("New client connected" + s.getInetAddress().getHostAddress());   
                                         
            ClientHandler clientSock = new ClientHandler(s);

            threadPool.execute(clientSock);
        }        
    }

    public static boolean register(String username) {
        if(users.contains(username))
            return false;
        
        return users.add(username);
    }

    public static String getAvailability() {
        return "AVAIL Gold:" + tickets.get("gold") + " Pit:" + tickets.get("pit") + " Parterre:" + tickets.get("parterre");
    }
}