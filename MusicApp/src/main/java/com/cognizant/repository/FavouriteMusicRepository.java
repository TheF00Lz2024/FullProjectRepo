package com.cognizant.repository;

import com.cognizant.model.UserFavouriteSongList;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavouriteMusicRepository extends CrudRepository<UserFavouriteSongList, String> {
    @Query(value = "SELECT track_id, user_id, track_name, track_image_url, artist_name from user_favourite_song_list WHERE track_id=:trackId AND user_id=:userId", nativeQuery = true)
    List<UserFavouriteSongList> findDuplicateUserFavouriteSong(@Param("trackId") String trackId, @Param("userId") String userId);

    @Query(value = "SELECT track_id, user_id, track_name, track_image_url, artist_name from user_favourite_song_list WHERE user_id=:userId", nativeQuery = true)
    List<UserFavouriteSongList> getAllUserFavouriteSong(@Param("userId") String userId);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_favourite_song_list WHERE track_id=:trackId AND user_id=:userId", nativeQuery = true)
    void deleteUserFavouriteSong(@Param("trackId") String trackId, @Param("userId") String userId);
}
