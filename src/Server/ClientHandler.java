package Server;

import Exceptions.ClientNameAlreadyInUseException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ClientHandler extends Thread {

    private Scanner input;
    private PrintWriter output;
    private Socket socket;
    private MSNServer server;

    public ClientHandler(Socket socket, MSNServer server) throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        Boolean closeConnection = false;

        do {
            String msgInput = input.next();
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
                    System.out.println("Message from: " + this.getName() + ": " + msg);
                    break;
                case "USER":
                    String clientName = split.next();
                    HashMap userHashMap = server.getUserHashMap();
                    try {
                        if (userHashMap == null || !userHashMap.containsKey(clientName)) {
                            this.setName(clientName);
                            server.addUser(clientName, this);
                        } else {
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
