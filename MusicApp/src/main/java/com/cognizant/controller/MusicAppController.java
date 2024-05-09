package com.cognizant.controller;

import com.cognizant.config.SpotifyConfig;
import com.cognizant.exception.FavouriteSongAlreadyExist;
import com.cognizant.exception.FavouriteSongNotFound;
import com.cognizant.model.SpotifySong;
import com.cognizant.model.SpotifyTokenGenerator;
import com.cognizant.model.UserFavouriteSongList;
import com.cognizant.service.UserFavouriteSongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MusicAppController {
    Logger logger = LoggerFactory.getLogger(MusicAppController.class);
    private SpotifyConfig spotifyConfig;
    private UserFavouriteSongService userFavouriteSongService;
    public MusicAppController(SpotifyConfig spotifyConfig, UserFavouriteSongService userFavouriteSongService) {
        this.spotifyConfig = spotifyConfig;
        this.userFavouriteSongService = userFavouriteSongService;
    }
    //initial API to verify user
    @GetMapping("/authenticate-login")
    public String getSensitiveAdminData() {
        return "{ \"authorizationMessage\": \"You have login successfully\"}";
    }
    //API for generating Spotify Token
    @GetMapping("/spotify-token")
    public ResponseEntity<SpotifyTokenGenerator> getSpotifyToken(){
        return spotifyConfig.getToken();
    }
    // API for sending recommended song back to user
    @GetMapping("/spotify-recommended-music")
    public ResponseEntity<ArrayList<SpotifySong>> getRecommendedTrack(@RequestHeader("spotify-token") String token){
        return spotifyConfig.getRecommendedTrack(token);
    }
    //API for searching track by title
    @GetMapping("/spotify-search-music/{Track}")
    public ResponseEntity<ArrayList<SpotifySong>> getSearchTrack(@RequestHeader("spotify-token") String token, @PathVariable("Track") String track){
        return spotifyConfig.getUserSearchTrack(token, track);
    }
    //API for adding user favourite song
    @PostMapping("/add-favourite-music")
    public ResponseEntity<UserFavouriteSongList> addUserFavourite(@RequestBody UserFavouriteSongList userFavouriteSongList){
        return new ResponseEntity<>(userFavouriteSongService.addUserFavouriteSong(userFavouriteSongList), HttpStatus.CREATED);
    }
    //API for getting user favourite song
    @GetMapping("/get-favourite-music")
    public ResponseEntity<List<UserFavouriteSongList>> getUserFavourite(@RequestParam("userId")String userId){
        return new ResponseEntity<>(userFavouriteSongService.getAllUserFavouriteList(userId), HttpStatus.OK);
    }
    //API for deleteing user favourite song
    @DeleteMapping("/delete-favourite-music")
    public ResponseEntity<UserFavouriteSongList> deleteUserFavourite(@RequestParam("trackId")String trackId, @RequestParam("userId")String userId){
        return new ResponseEntity<>(userFavouriteSongService.deleteUserFavouriteSong(trackId, userId), HttpStatus.OK);
    }

    @ExceptionHandler(value = FavouriteSongAlreadyExist.class)
    public ResponseEntity<String> duplicateFavouriteSong(FavouriteSongAlreadyExist message){
        logger.error(message.getMessage());
        return new ResponseEntity<>(message.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = FavouriteSongNotFound.class)
    public ResponseEntity<String> noSuchFavourite(FavouriteSongNotFound message){
        logger.error(message.getMessage());
        return new ResponseEntity<>(message.getMessage(), HttpStatus.NOT_FOUND);
    }
}
