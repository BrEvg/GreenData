
package ru.greendata.exceptions;
 
public class DepositAlreadyExistException extends Exception {
     
    private static final long serialVersionUID = -3132181624799363221L;
     
    public DepositAlreadyExistException(String message) {
        super(message);
    }
     
}