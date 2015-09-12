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
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
    private ConcurrentMap<ClientHandler, String> userHashMap = new ConcurrentHashMap();

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
                    userHashMap.put(ch, ch.toString());
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
        this.sendToAll("USERLIST#" + getClientUserList());
    }

    public List<ClientHandler> getClientList() {
        return clientList;
    }

    public synchronized void sendToAll(String msg) {
        for (ClientHandler clientList1 : clientList) {
            clientList1.send(msg);
        }
    }
    
    public synchronized void sendToUsers(String msg, List<String> recievers) {
        for (String reciever : recievers) {
            for (ClientHandler clientList1 : clientList) {
                if(clientList1.getName().equals(reciever)) {
                    clientList1.send(msg);
                }
            }
        }
    }

    public synchronized boolean addUser(ClientHandler client, String name) {
        String userName = name;
        while(userHashMap.containsValue(userName)) {
            Random ran = new Random();
            userName+=(ran.nextInt(100) + 1) + "";
        }
        userHashMap.replace(client, userName);
        this.sendToAll("USERLIST#" + getClientUserList());
        return true;
    }
    
    public synchronized void removeUser(ClientHandler client, String name) {
        userHashMap.remove(client, name);
    }

    public String getClientUserList() {
        String clientUserList = "";
        for (String userHashMap1 : userHashMap.values()) {
            clientUserList+=userHashMap1+",";
        }
        return clientUserList;
        
        
    }

    public void stopServer() {
        running = false;
    }

    //METHODS ONLY TO BE USED BY JUNIT TEST
    public Boolean getRunning() {
        return running;
    }

}
