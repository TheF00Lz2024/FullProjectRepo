package com.cognizant.model.recomendedSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"spotify"}, ignoreUnknown = true)
@Data
public class ExternalUrls {
    private String spotify;
}
