package com.cognizant.model.recomendedSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"isrc"}, ignoreUnknown = true)
@Data

public class ExternalIds {
    private String isrc;
}
