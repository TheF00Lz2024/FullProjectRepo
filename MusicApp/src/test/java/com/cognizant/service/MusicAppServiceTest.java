package com.cognizant.service;

import com.cognizant.model.User;
import com.cognizant.model.UserFavouriteSongList;
import com.cognizant.repository.FavouriteMusicRepository;
import com.cognizant.service.UserFavouriteSongServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicAppServiceTest {
    @Mock
    FavouriteMusicRepository favouriteMusicRepository;

    @InjectMocks
    UserFavouriteSongServiceImpl userFavouriteSongService;

    List<UserFavouriteSongList> favouriteSongLists;

    UserFavouriteSongList userFavouriteSongList;

    @BeforeEach
    void setUp(){
        userFavouriteSongList = UserFavouriteSongList.builder()
                                .trackId("NewTrack")
                                .userId(User.builder()
                                        .username("Test@gmail.com")
                                        .password("123456789")
                                        .build())
                                .trackImageUrl("www.google.com")
                                .trackName("NewTrackName")
                                .artistName("NewTrackArtists")
                                .build();
    }

    @Test
    void givenSongShouldSave(){
        when(favouriteMusicRepository.save(any())).thenReturn(userFavouriteSongList);
        assertEquals(userFavouriteSongList, favouriteMusicRepository.save(userFavouriteSongList));
    }

    @Test
    void shouldReturnAllSong(){
        when(favouriteMusicRepository.getAllUserFavouriteSong(
                userFavouriteSongList.getUserId().getUsername())).thenReturn(favouriteSongLists);
        List<UserFavouriteSongList> mockList = favouriteMusicRepository.getAllUserFavouriteSong(
                                                userFavouriteSongList.getUserId().getUsername());

        assertEquals(mockList,favouriteSongLists);
        verify(favouriteMusicRepository, times(1)).getAllUserFavouriteSong(userFavouriteSongList.getUserId().getUsername());
    }

    @Test
    void shouldReturnDeleteSong(){
        favouriteMusicRepository.deleteUserFavouriteSong(userFavouriteSongList.getTrackId(),
                userFavouriteSongList.getUserId().getUsername());

        verify(favouriteMusicRepository, times(1)).deleteUserFavouriteSong(
                userFavouriteSongList.getTrackId(),
                userFavouriteSongList.getUserId().getUsername());
    }
}
