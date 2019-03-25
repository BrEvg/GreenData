
package ru.greendata.exceptions;
 
public class ClientAlreadyExistException extends Exception {
     
    private static final long serialVersionUID = -3132181624725369411L;
     
    public ClientAlreadyExistException(String message) {
        super(message);
    }
     
}