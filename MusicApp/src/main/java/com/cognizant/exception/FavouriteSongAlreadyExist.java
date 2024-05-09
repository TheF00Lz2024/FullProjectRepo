package com.cognizant.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class FavouriteSongAlreadyExist extends DataIntegrityViolationException {
    public FavouriteSongAlreadyExist(String msg) {
        super(msg);
    }
}
