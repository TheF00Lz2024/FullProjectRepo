package com.cognizant.model.recomendedSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(value = {"external_urls","href","id","type","uri"}, ignoreUnknown = true)
@Data
public class Artist {
    private ExternalUrls external_urls;
    private String href;
    private String id;
    private String name;
    private String type;
    private String uri;
}
