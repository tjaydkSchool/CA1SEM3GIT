package Server;

import Exceptions.ClientNameAlreadyInUseException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

public class ClientHandler extends Observable implements Runnable {

    private Scanner input;
    private PrintWriter output;
    private Socket socket;
    private MSNServer server;
    private String name;
    private String switchString;

    public ClientHandler(Socket socket, MSNServer server) throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.server = server;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public void run() {
        Boolean closeConnection = false;

        do {
            String msgInput = input.next();
            System.out.println("Message from ClientHandler: " + msgInput);
            List<String> recieverList = new ArrayList();

            Scanner split = new Scanner(msgInput).useDelimiter("#");
            String action = split.next();
            String msg;

            switch (action) {
                case "STOP":
                    closeConnection = true;
                    break;
                case "MSG":
                    String recievers = split.next();
                    Scanner listRecievers = new Scanner(recievers).useDelimiter(",");
                    while (listRecievers.hasNext()) {
                        recieverList.add(listRecievers.next());
                    }
                    msg = split.next();
                    server.send("Message from: " + this.getName() + ": " + msg);
                    break;
                case "USER":
                    String clientName = split.next();
                    HashMap userHashMap = server.getUserHashMap();
                    try {
                        if (userHashMap == null || !userHashMap.containsKey(clientName)) {
                            this.setName(clientName);
                            server.addUser(clientName, this);
                        } else {
                            output.println("Username already taken, please choose another");
                            throw new ClientNameAlreadyInUseException();
                        }

                    } catch (ClientNameAlreadyInUseException ex) {
                        System.out.println("Username already in use");
                    }
            }
        } while (!closeConnection);
    }

    public void send(String msg) {
        output.println(msg);
    }

}
