package com.cognizant.service;

import com.cognizant.model.UserFavouriteSongList;

import java.util.List;

public interface UserFavouriteSongService {
    UserFavouriteSongList addUserFavouriteSong(UserFavouriteSongList userFavouriteSongList);
    List<UserFavouriteSongList> getAllUserFavouriteList(String userId);
    UserFavouriteSongList deleteUserFavouriteSong(String trackId, String userId );
}
