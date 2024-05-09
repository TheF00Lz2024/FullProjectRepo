package com.cognizant.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateEmailException extends DataIntegrityViolationException {
    public DuplicateEmailException(String message){
        super(message);
    }
}
