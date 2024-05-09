import { AfterViewInit, Component, EventEmitter, Output, ViewChild } from '@angular/core';
import { mergeMap } from 'rxjs';
import { MusicAppAPIService } from '../Service/music-app-api.service';
import { SessionServiceService } from '../Service/session-service.service';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { showTrackDetail, spotifySong, UserFavouriteSongList } from '../Modal/spotifyInterface';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { user } from '../Modal/apiResponse';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-recommended-music',
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule,MatButtonModule, MatProgressSpinnerModule, CommonModule],
  templateUrl: './recommended-music.component.html',
  styleUrl: './recommended-music.component.css'
})
export class RecommendedMusicComponent implements AfterViewInit{

  //generate table using Angular Material
  dataSource = new MatTableDataSource<spotifySong>([]);
  //as a flag to set the animation of Angular spinner
  dataloading: boolean = true;

  //to send data to parent (welcome-page component) to inform the track user chosen
  @Output() getTrackDetail = new EventEmitter<showTrackDetail>();

  //generate constructor for API and session Service
  constructor(private recomendedMusicService: MusicAppAPIService, private sessionService: SessionServiceService){}

  displayedColumns: string[] = ['id', 'trackName', 'ArtistName', 'addToFavourite'];

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  //will run when component initialize
  ngAfterViewInit() {
    this.getRecommendedMusic();
  }
  
  //function to generate recommended track to user
  getRecommendedMusic(){
    this.dataloading = true;
    //call API Service and get spotify token, and passing JWT token as a form of authentication
    this.recomendedMusicService.getSpotifyToken(this.sessionService.getAuthenticationSession()!)
      .pipe(mergeMap((data) => {
        console.log(data.access_token)
        //once there is no issue, will call the Service again and pass the create Spotify API token back to web server with JWT token 
        return this.recomendedMusicService.getSpotifyRecomendedSong(this.sessionService.getAuthenticationSession()!,data.access_token)
      })).subscribe({
        next:(data) => {
          //if there is no issue assign the genreated data to the table datasource
          this.dataSource = new MatTableDataSource<spotifySong>(data);
          this.dataSource.paginator = this.paginator;
          this.dataloading = false;
        }
      });
  }

  //button function for adding track to list
  addToFavourite(data: spotifySong){
    this.dataloading = true;
    // create a new user obj
    const newUser = {
      username: this.sessionService.getUsernameSession(),
      password: ''
    } as user;

    // create a new UserFavouriteSOngList obj
    const newData = {
      trackId: data.id,
      userId: newUser,
      trackName: data.trackName,
      trackImageUrl: data.trackImageUrl,
      artistName: data.artistName
    } as UserFavouriteSongList;

    // pass the created UserFavouriteSongList obj to API service to store the track in favourite list
    this.recomendedMusicService.addToFavouriteList(this.sessionService.getAuthenticationSession()!, newData)
      .subscribe({
        next:(data) => {
          //show an alert once the track has been added
          alert(`Track have been added to favourite! \nTrack detail \nTrack Name: ${data.trackName} \nArtist Name: ${data.artistName}`);
          this.dataloading = false;
        }, error: ((err: HttpErrorResponse) => {
          if(err.status === 403){
            //show an alert if this track is already in their favourite list
            alert('This track already have been added to favourite list!')
            console.log('Duplicate track being added!');
            console.log(`Track detail: ${newData}`)
          }else{
            //show an error is an unknown error had occur
            alert('Error has occur!');
            console.log(`Error had occur: ${err}`);
          }
          this.dataloading = false;
        })
      })
  }

  // pass data to parent (based on the track the user selected)
  getTrackData(data: spotifySong){
    this.getTrackDetail.emit({
      trackDetail: data,
      backButtonPress: true,
      // as a form of holder to inform parent where the user select the track
      pageIndex: 0
    });
  }
}
