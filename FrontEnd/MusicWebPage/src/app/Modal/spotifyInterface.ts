import { user } from "./apiResponse"

export interface spotifyToken{
  access_token: string,
  token_type: string,
  expires_in: number
}

export interface spotifySong {
  id: string,
  trackImageUrl: string,
  trackName: string,
  artistName: string
}

export interface UserFavouriteSongList{
  trackId: string,
  userId: user,
  trackImageUrl: string,
  trackName: string,
  artistName: string
}

export interface showTrackDetail{
  trackDetail: spotifySong,
  backButtonPress: boolean,
  pageIndex: number
}
