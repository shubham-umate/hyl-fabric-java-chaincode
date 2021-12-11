package org.hyperledger.fabric.samples.product;

import org.hyperledger.fabric.shim.ChaincodeException;

public class ProductException extends ChaincodeException {

    private static final long serialVersionUID = 3664437023130016393L;
    int code;

    public ProductException(){}
    public ProductException(String message,Throwable cause, int code){
        super(message,cause);
        this.code = code;
    }

    public ProductException(String message, int code){
        super(message);
        this.code = code;
    }

}
