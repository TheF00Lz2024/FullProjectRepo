import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { authorization, loginToken, user } from '../Modal/apiResponse';
import { spotifySong, spotifyToken, UserFavouriteSongList } from '../Modal/spotifyInterface';

@Injectable({
  providedIn: 'root'
})
export class MusicAppAPIService {
  //for login and sign up api
  loginAPIUrl = 'http://localhost:8081/api/v1/login';
  //for authenticate JWT token, and spotify rec and search song and also getting and deleteing user favourite track list
  MusicAppAPIUrl = 'http://localhost:8082/api/v1'

  // for user login
  constructor(private httpClient: HttpClient) { }
  getUserLogin(username: string, password: string): Observable<loginToken> {
    return this.httpClient.get<loginToken>(`${this.loginAPIUrl}?username=${username}&password=${password}`);
  }

  // once login call server and ask for jwt token
  authorizeUserLogin(token: string): Observable<authorization> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);
    return this.httpClient.get<authorization>(`${this.MusicAppAPIUrl}/authenticate-login`, { 'headers': headers });
  }

  // for creating user account
  createAccount(object: user): Observable<user> {
    const headers = { 'content-type': 'application/json' }
    const body = JSON.stringify(object);
    return this.httpClient.post<user>(`${this.loginAPIUrl}`, body, { 'headers': headers });
  }

  // for generating spotify auth token from server
  getSpotifyToken(token: string): Observable<spotifyToken> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);
    return this.httpClient.get<spotifyToken>(`${this.MusicAppAPIUrl}/spotify-token`, { 'headers': headers });
  }

  // for generating recomended song
  getSpotifyRecomendedSong(token: string, spotifytoken: string): Observable<spotifySong[]> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('spotify-token', spotifytoken);
    return this.httpClient.get<spotifySong[]>(`${this.MusicAppAPIUrl}/spotify-recommended-music`, { 'headers': headers });
  }

  // get the generated spotify auth token and search for music via user input
  getSearchMusicTrack(token: string, spotifytoken: string, track: string): Observable<spotifySong[]> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('spotify-token', spotifytoken);
    return this.httpClient.get<spotifySong[]>(`${this.MusicAppAPIUrl}/spotify-search-music/${track}`, { 'headers': headers });
  }

  // add song to favourite list
  addToFavouriteList(token: string, object: UserFavouriteSongList): Observable<UserFavouriteSongList> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);
    return this.httpClient.post<UserFavouriteSongList>(`${this.MusicAppAPIUrl}/add-favourite-music`, object, { 'headers': headers });
  }

  // get all user song from favourite list
  getFavouriteList(token: string, userId: string): Observable<UserFavouriteSongList[]> {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);
    return this.httpClient.get<UserFavouriteSongList[]>(`${this.MusicAppAPIUrl}/get-favourite-music`, { 'headers': headers, 'params': { 'userId': userId } });
  }

  // delete user song from favourite list
  deleteFavouriteList(token: string, trackId: string, userId: string) {
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);
    return this.httpClient.delete<UserFavouriteSongList>(`${this.MusicAppAPIUrl}/delete-favourite-music`, { 'headers': headers, 'params': { 'trackId': trackId, 'userId': userId } });
  }
}
