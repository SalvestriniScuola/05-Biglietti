package dev.lieno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket s;

    public ClientHandler(Socket socket)
        {
            this.s = socket;
        }

    @Override
    public void run() {
        // Socket input output
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);

            out.println("WELCOME");
            String cmd;
            String username = "";

            do {
               cmd = in.readLine().toLowerCase();

                if (username.isBlank()) {
                    switch (cmd.split(" ")[0]) {
                        case "login":
                            if(Main.register(cmd.split(" ")[1])) {
                                username = cmd.split(" ")[1];
                                out.println("OK");
                            } else
                                err(out, "USERINUSE");
                            break;
                    
                        default:
                            err(out, "LOGINREQUIRED");
                            break;
                    }
                } else {
                    switch (cmd.split(" ")[0]) {
                        case "n":
                            out.println(Main.getAvailability());
                            break;
                        
                        case "buy":
                                String tmp = in.readLine().toLowerCase();

                            break;
                        default:
                            break;
                    }
                }

            } while (!cmd.equals("QUIT"));
        

        System.out.println("Client disconnected.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void err(PrintWriter out, String message) {
        out.println("ERR " + message);
    }
}