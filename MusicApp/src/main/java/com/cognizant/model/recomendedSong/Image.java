package com.cognizant.model.recomendedSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"height","width"}, ignoreUnknown = true)
@Data
public class Image {
    private int height;
    private String url;
    private int width;
}
