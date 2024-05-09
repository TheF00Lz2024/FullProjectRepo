package com.cognizant.model.recomendedSong;

import lombok.Data;
@Data
public class Track {
    private Album album;
    private Artist2[] artists;
    private String [] available_markets;
    private int disc_number;
    private int duration_ms;
    private boolean explicit;
    private ExternalIds external_ids;
    private ExternalUrls4 external_urls;
    private String href;
    private String id;
    private boolean is_local;
    private String name;
    private int popularity;
    private String preview_url;
    private int track_number;
    private String type;
    private String uri;
}
