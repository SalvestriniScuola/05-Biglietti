package dev.lieno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable{

    private Socket s;
    private Map<String,Integer> tickets;

    public ClientHandler(Socket socket, Map<String,Integer> tickets)
        {
            this.s = socket;
            this.tickets = tickets;
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
                            out.println("AVAIL Gold:" + tickets.get("gold") + " Pit:" + tickets.get("pit") + " Parterre:" + tickets.get("parterre"));
                            break;
                        
                        case "buy":
                                String[] token = in.readLine().toLowerCase().split(" ");
                                
                                
                                Integer ammount = Integer.parseInt(token[1]);
                                

                                switch (token[0]) {
                                    case "pit":
                                        if(tickets.get("pit") < ammount)
                                            err(out, "KO");
                                        else {
                                            tickets.put("pit", tickets.get("pit") - ammount);
                                            out.println("OK");
                                        }
                                        break;
                                
                                    case "gold":

                                        if(tickets.get("gold") < ammount)
                                            err(out, "KO");
                                        else {
                                            tickets.put("gold", tickets.get("gold") - ammount);
                                            out.println("OK");
                                        }
                                        break;

                                    case "parterre":
                                        if(tickets.get("parterre") < ammount)
                                            err(out, "KO");
                                        else {
                                            tickets.put("parterre", tickets.get("parterre") - ammount);
                                            out.println("OK");
                                        }

                                        break;

                                    default:
                                        err(out, "KO");
                                        break;
                                }

                            break;
                        default:
                            err(out, "command not found!");
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