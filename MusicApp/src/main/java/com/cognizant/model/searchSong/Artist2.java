package com.cognizant.model.searchSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"external_urls","href","id","name","type","uri"}, ignoreUnknown = true)
@Data
public class Artist2 {
    private ExternalUrls3 external_urls;
    private String href;
    private String id;
    private String name;
    private String type;
    private String uri;
}
