package com.cognizant.model.searchSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"href","limit","next","offset","previous","number"}, ignoreUnknown = true)
@Data
public class Tracks {
    private String href;
    private Item[] items;
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int number;
}
