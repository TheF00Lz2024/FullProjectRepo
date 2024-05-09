package com.cognizant.model.searchSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"spotify"}, ignoreUnknown = true)
@Data
public class ExternalUrls2 {
    private String spotify;
}
