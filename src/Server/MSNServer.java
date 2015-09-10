/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Dennis
 */
public class MSNServer {

    public static final Properties props = Utility.initProperties("server.properties");
    private boolean running = true;
    private static ServerSocket serverSocket;
    private List<ClientHandler> clientList = new ArrayList();
    private HashMap userHashMap = new HashMap();

//    SERVERLOGFILE
    public static Logger log = Logger.getLogger(MSNServer.class.getName());

    public static void main(String[] args) {
        new MSNServer().startServer();
    }

    public void startServer() {
        FileHandler fh = null;
        SimpleFormatter format = new SimpleFormatter();
        try {
            fh = new FileHandler("log.log");
            log.addHandler(fh);
            fh.setFormatter(format);

        } catch (IOException ex) {
            Logger.getLogger(MSNServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        FIX THIS SO IT LOOKS BETTER

        try {
            log.info("First message in log");

            String ip = props.getProperty("serverIp");
            int port = Integer.parseInt(props.getProperty("port"));
            try {
                ServerSocket ss = new ServerSocket();
                ss.bind(new InetSocketAddress(ip, port));
                do {
                    Socket socket = ss.accept();
                    ClientHandler ch = new ClientHandler(socket, this);
                    Thread t = new Thread(ch);
                    t.start();
                    clientList.add(ch);
                } while (running);
            } catch (IOException ex) {
                Logger.getLogger(MSNServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SecurityException ex) {
            Logger.getLogger(MSNServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fh.close();
        }
    }

    public void removeClient(ClientHandler ch) {
        clientList.remove(ch);
    }

    public List<ClientHandler> getClientList() {
        return clientList;
    }

    public void send(String msg) {
        for (ClientHandler clientList1 : clientList) {
            clientList1.send(msg);
        }
    }

    public void addUser(String user, Object o) {
        userHashMap.put(user, o);
    }

    public HashMap getUserHashMap() {
        return userHashMap;
    }

    public void stopServer() {
        running = false;
    }

    //METHODS ONLY TO BE USED BY JUNIT TEST
    public Boolean getRunning() {
        return running;
    }

}
