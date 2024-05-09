package com.cognizant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="User")
public class User {
    @Id
    private String username;
    private String password;
    @OneToMany(mappedBy = "trackId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserFavouriteSongList> userFavouriteSongList;
}

