package com.cognizant.model;

import lombok.Data;

@Data
public class SpotifyTokenGenerator {
    private String access_token;
    private String token_type;
    private int expires_in;
}
