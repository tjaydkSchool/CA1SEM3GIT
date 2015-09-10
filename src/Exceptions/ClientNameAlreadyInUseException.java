package Exceptions;

public class ClientNameAlreadyInUseException extends Exception{
    public ClientNameAlreadyInUseException() {
        
    }
    
    public ClientNameAlreadyInUseException(String message) {
        super(message);
    }
}
