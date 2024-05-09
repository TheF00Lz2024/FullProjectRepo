package com.cognizant.service;

import com.cognizant.exception.FavouriteSongAlreadyExist;
import com.cognizant.exception.FavouriteSongNotFound;
import com.cognizant.model.UserFavouriteSongList;
import com.cognizant.repository.FavouriteMusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserFavouriteSongServiceImpl")
public class UserFavouriteSongServiceImpl implements UserFavouriteSongService{
    FavouriteMusicRepository favouriteMusicRepository;
    @Autowired
    public UserFavouriteSongServiceImpl(FavouriteMusicRepository favouriteMusicRepository) {
        this.favouriteMusicRepository = favouriteMusicRepository;
    }

    @Override
    public UserFavouriteSongList addUserFavouriteSong(UserFavouriteSongList userFavouriteSongList) {
        String userId = userFavouriteSongList.getUserId().getUsername();
        int found = favouriteMusicRepository.findDuplicateUserFavouriteSong(userFavouriteSongList.getTrackId(), userId).size();
        if(found == 0){
            favouriteMusicRepository.save(userFavouriteSongList);
            return userFavouriteSongList;
        } else{
            throw new FavouriteSongAlreadyExist("{ \"errorMessage\": \"This song is already add to favourite!\"}");
        }
    }

    @Override
    public List<UserFavouriteSongList> getAllUserFavouriteList(String userId) {
        return favouriteMusicRepository.getAllUserFavouriteSong(userId);
    }

    @Override
    public UserFavouriteSongList deleteUserFavouriteSong(String trackId, String userId) {
        List<UserFavouriteSongList> found = favouriteMusicRepository.findDuplicateUserFavouriteSong(trackId, userId);
        if(found.isEmpty()){
            throw new FavouriteSongNotFound("{ \"errorMessage\": \"This favourite does not exist!\"}");
        } else{
            favouriteMusicRepository.deleteUserFavouriteSong(trackId, userId);
            return found.get(0);
        }
    }
}
