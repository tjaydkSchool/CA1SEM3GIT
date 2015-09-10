/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asnorrason
 */
public class Client extends Observable implements Runnable {

    private String msg = "";
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private InetAddress serverAddress;
    private List<Observer> observerList = new ArrayList();
    private Client client = this;
    private int port;

    public void connect(String ip, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(ip);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        run();
    }

    public void send(String sendMsg) {
        output.println(sendMsg);
    }

    public void receive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    Object msg = input.nextLine();
                    System.out.println("Message from client: " + msg);
                    if(msg.equals("STOP#")) {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    for (Observer observerList1 : observerList) {
                        observerList1.update(client, msg);
                    }
                    setChanged();
                    notifyObservers(msg);
                }
            }
        });
        t1.start();

    }

    public String getMsg() {
        return msg;
    }

    @Override
    public void addObserver(Observer o) {
        observerList.add(o);
    }
}
