package com.cognizant.exception;

import java.util.NoSuchElementException;

public class FavouriteSongNotFound extends NoSuchElementException {
    public FavouriteSongNotFound(String message){
        super(message);
    }
}
