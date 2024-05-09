package com.cognizant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="UserFavouriteSongList")
public class UserFavouriteSongList {
    @Id
    private String trackId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;
    private String trackImageUrl;
    private String trackName;
    private String artistName;
}
