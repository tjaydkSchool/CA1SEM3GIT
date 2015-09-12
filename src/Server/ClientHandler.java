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
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends Observable implements Runnable {

    private Scanner input;
    private PrintWriter output;
    private Socket socket;
    private MSNServer server;
    private String name;
    private String switchString;
    private ClientHandler ch = this;

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
            String msgInput = input.nextLine();

            Scanner split = new Scanner(msgInput).useDelimiter("#");
            String action = split.next();
            String msg;

            switch (action) {
                case "STOP":
                    closeConnection = true;
                    server.removeUser(ch, ch.getName());
                    server.removeClient(ch);
                    break;
                case "MSG":
                    List<String> recieverList = new ArrayList();
                    String recievers = split.next();
                    Scanner listRecievers = new Scanner(recievers).useDelimiter(",");
                    if (!recievers.equals("*")) {
                        while (listRecievers.hasNext()) {
                            recieverList.add(listRecievers.next());
                        }
                        msg = split.nextLine();
                        server.sendToUsers(("MSG#" + ch.getName() + msg), recieverList);
                        break;
                    }
                    msg = split.nextLine();
                    server.sendToAll("MSG#" + ch.getName() + msg);
                    break;
                case "USER":
                    String clientName = split.next();
                    server.addUser(ch, clientName);
                    ch.setName(clientName);
                    break;
            }
        } while (!closeConnection);
    }

    public void send(String msg) {
        output.println(msg);
    }

}
