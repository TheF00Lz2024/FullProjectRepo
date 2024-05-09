package com.cognizant.model;

public enum SpotifyConfig {
    SPOTIFY_ID("f43d02be03bc42339758dc9d32b8e6a3"),
    SPOTIFY_SECRET("a69317fcc9d0473d98aea5619fc74351");
    private String value;
    private SpotifyConfig(String value){
        this.value = value;
    }
    @Override
    public String toString(){
        return value;
    }
}
