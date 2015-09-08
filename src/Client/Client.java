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
import java.util.Scanner;

/**
 *
 * @author Asnorrason
 */
public class Client {

    private String msg = "";
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private InetAddress serverAddress;
    private int port;

    public void connect(String ip, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(ip);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String sendMsg) {
        msg = sendMsg;
    }

    public void receive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public String getMsg() {
        return msg;
    }

}
