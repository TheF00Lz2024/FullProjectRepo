package com.cognizant.config;

import com.cognizant.model.*;
import com.cognizant.model.recomendedSong.RootRec;
import com.cognizant.model.searchSong.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static com.cognizant.model.SpotifyConfig.SPOTIFY_ID;
import static com.cognizant.model.SpotifyConfig.SPOTIFY_SECRET;

@Service
public class SpotifyConfig {
    private static final String CLIENT_ID = SPOTIFY_ID.toString();
    private static final String CLIENT_SECRET = SPOTIFY_SECRET.toString();
    private static final String SPOTIFY_URL = "https://accounts.spotify.com/api/token";

    private final RestTemplate restTemplate;
    public SpotifyConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //Ref https://reflectoring.io/spring-resttemplate/
    public ResponseEntity<SpotifyTokenGenerator> getToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity( SPOTIFY_URL, request , SpotifyTokenGenerator.class);
    }
    public ResponseEntity<ArrayList<SpotifySong>> getRecommendedTrack(String spotifyToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+ spotifyToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String searchTrackURL = "https://api.spotify.com/v1/recommendations?limit=20&seed_artists=4NHQUGzhtTLFvgF5SZesLK&seed_genres=classical%2Ccountry&seed_tracks=0c6xIDDpzE81m2q797ordA";
        ResponseEntity<RootRec> response = restTemplate.exchange(searchTrackURL, HttpMethod.GET, entity, RootRec.class);
        ArrayList<RootRec> trackList = new ArrayList<>();
        trackList.add(response.getBody());
        ArrayList<SpotifySong> getSpotifySong = new ArrayList<>();
        for(com.cognizant.model.recomendedSong.Track track : trackList.get(0).getTracks()){
            SpotifySong spotifySong = new SpotifySong();
            spotifySong.setId(track.getId());
            spotifySong.setTrackName(track.getName());
            for(com.cognizant.model.recomendedSong.Artist artist: track.getAlbum().getArtists()){
                spotifySong.setArtistName(artist.getName());
            }
            for(com.cognizant.model.recomendedSong.Image image: track.getAlbum().getImages()){
                spotifySong.setTrackImageUrl(image.getUrl());
            }
            getSpotifySong.add(spotifySong);
        }
        return new ResponseEntity<>(getSpotifySong, HttpStatus.OK);
    }
    public ResponseEntity<ArrayList<SpotifySong>> getUserSearchTrack(String spotifyToken, String trackName){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+ spotifyToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String searchTrackURL = "https://api.spotify.com/v1/search?q="+trackName+"&type=track&market=ES&limit=20&offset=0";
        ResponseEntity<Root> response = restTemplate.exchange(searchTrackURL, HttpMethod.GET, entity, Root.class);
        ArrayList<Root> trackList = new ArrayList<>();
        trackList.add(response.getBody());

        ArrayList<SpotifySong> getSpotifySong = new ArrayList<>();
        for(Item item : trackList.get(0).getTracks().getItems()){
            SpotifySong spotifySong = new SpotifySong();
            spotifySong.setId(item.getId());
            spotifySong.setTrackName(item.getName());
            for(Artist artist: item.getAlbum().getArtists()){
                spotifySong.setArtistName(artist.getName());
            }
            for(Image image: item.getAlbum().getImages()){
                spotifySong.setTrackImageUrl(image.getUrl());
            }
            getSpotifySong.add(spotifySong);
        }
        return new ResponseEntity<>(getSpotifySong, HttpStatus.OK);
    }
}
