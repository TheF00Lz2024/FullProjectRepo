package com.cognizant.model.recomendedSong;

import lombok.Data;

@Data
public class RootRec {
    private Track[] tracks;
    private Seed[] seeds;
}
