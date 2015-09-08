/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Dennis
 */
public class MSNServer {
    
    private boolean running = true;

    public static void main(String[] args) {

    }

    public void startServer() {

    }
    
    public void stopServer() {
        running = false;
    }
    
    
    
    //METHODS ONLY TO BE USED BY JUNIT TEST
    
    public Boolean getRunning() {
        return running;
    }

}
