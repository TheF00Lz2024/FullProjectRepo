package com.cognizant.repository;

import com.cognizant.model.User;
import com.cognizant.model.UserFavouriteSongList;
import com.cognizant.repository.FavouriteMusicRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class MusicAppRepositoryTest {

    @Autowired
    FavouriteMusicRepository favouriteMusicRepository;
    UserFavouriteSongList userFavouriteSongList;

    @BeforeEach
    void setUp(){
        userFavouriteSongList = UserFavouriteSongList.builder()
                .userId(User.builder()
                        .username("Test@gmail.com")
                        .password("123456789")
                        .build())
                .trackId("TestRepos")
                .trackName("Repos1")
                .trackImageUrl("www.google.com")
                .artistName("NA")
                .build();
    }
    @AfterEach
    void tearDown(){
        favouriteMusicRepository.deleteUserFavouriteSong(
                userFavouriteSongList.getTrackId(),
                userFavouriteSongList.getUserId().getUsername());
    }

    @Test
    void addAndGetSongList(){
        // add the song to db
        favouriteMusicRepository.save(userFavouriteSongList);
        List<UserFavouriteSongList> songlist = favouriteMusicRepository.getAllUserFavouriteSong(
                userFavouriteSongList.getUserId().getUsername());
        boolean found = false;
        //check if song have been added
        for(int i = 0; i<songlist.size(); i++){
            if(songlist.get(i).getTrackId().equals(userFavouriteSongList.getTrackId())){
                found = true;
            }
        }
        assertEquals(true, found);
    }

    @Test
    void addAndDeleteSongList(){
        UserFavouriteSongList newSong = UserFavouriteSongList.builder()
                                            .userId(User.builder()
                                                    .username("Test@gmail.com")
                                                    .password("123456789")
                                                    .build())
                                            .trackId("TestRepos")
                                            .trackName("Repos1")
                                            .trackImageUrl("www.google.com")
                                            .artistName("NA")
                                            .build();

        favouriteMusicRepository.save(newSong);
        favouriteMusicRepository.deleteUserFavouriteSong(
                newSong.getTrackId(),
                newSong.getUserId().getUsername());

        List<UserFavouriteSongList> songlist = favouriteMusicRepository.getAllUserFavouriteSong(
                                                newSong.getUserId().getUsername());
        boolean found = false;
        //check if song have been added
        for(int i = 0; i<songlist.size(); i++){
            if(songlist.get(i).getTrackId().equals(newSong.getTrackId())){
                found = true;
            }
        }
        assertEquals(false, found);
    }
}
