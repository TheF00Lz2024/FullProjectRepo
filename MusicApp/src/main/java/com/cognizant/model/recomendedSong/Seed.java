package com.cognizant.model.recomendedSong;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties({"initialPoolSize","afterFilterSize","afterRelinkingSize","id","type","href"})
@Data
public class Seed {
    private int initialPoolSize;
    private int afterFilteringSize;
    private int afterRelinkingSize;
    private int id;
    private String type;
    private String href;

}
