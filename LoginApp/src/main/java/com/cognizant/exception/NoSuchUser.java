package com.cognizant.exception;

import java.util.NoSuchElementException;

public class NoSuchUser extends NoSuchElementException {
    public NoSuchUser(String message){
        super(message);
    }
}
