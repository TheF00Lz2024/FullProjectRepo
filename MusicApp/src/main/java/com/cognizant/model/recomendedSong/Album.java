package com.cognizant.model.recomendedSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"album_type","available_market","external_urls","href","id","name","release_date","release_date_precision",
"total_tracks","type","uri"}, ignoreUnknown = true)
@Data
public class Album {
    private String album_type;
    private Artist[] artists;
    private String[] available_markets;
    private ExternalUrls2 external_urls;
    private String href;
    private String id;
    private Image[] images;
    private String name;
    private String release_date;
    private String release_date_precision;
    private int total_tracks;
    private String type;
    private String uri;
}
