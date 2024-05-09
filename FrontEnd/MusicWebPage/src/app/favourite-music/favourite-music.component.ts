import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, EventEmitter, Output, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { showTrackDetail, spotifySong, UserFavouriteSongList } from '../Modal/spotifyInterface';
import { MusicAppAPIService } from '../Service/music-app-api.service';
import { SessionServiceService } from '../Service/session-service.service';

@Component({
  selector: 'app-favourite-music',
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule,MatButtonModule, MatProgressSpinnerModule, CommonModule],
  templateUrl: './favourite-music.component.html',
  styleUrl: './favourite-music.component.css'
})
export class FavouriteMusicComponent implements AfterViewInit{
  
  dataSource = new MatTableDataSource<UserFavouriteSongList>([]);
  //for data loading animation to tell user the webpage is laodign
  dataloading: boolean = true;
  //a flag to tell user if there is track in their favorutie list or not
  havedata: boolean = true;
  //emit a event if the user click on the track ID and pass it to the parent component (welcome page)
  @Output() getTrackDetail = new EventEmitter<showTrackDetail>();

  //call API service and session service
  constructor(private recomendedMusicService: MusicAppAPIService, private sessionService: SessionServiceService){}

  displayedColumns: string[] = ['id', 'trackName', 'ArtistName', 'removeFromFavourite'];

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  //will fire this function after page had been initialized
  ngAfterViewInit(): void {
    this.getFavouriteMusic();
  }

  // function to get all favourite track from server
  getFavouriteMusic(){
    this.dataloading = true;
    //call the API service to get all suer favoruite song list and passing JWT token
    this.recomendedMusicService.getFavouriteList(this.sessionService.getAuthenticationSession()!, this.sessionService.getUsernameSession()!)
      .subscribe({
        next: (data) => {
          // to check if there is any track in their favourite track list
          if(data.length>0){
            //if their is pass the data to table datasource tio render the table
            this.dataSource = new MatTableDataSource<UserFavouriteSongList>(data);
            this.dataSource.paginator = this.paginator;
            this.havedata = true;
          }else{
            //if there is no track will change the flag to false
            this.havedata = false;
          }
          //will only hide once condition have been finish checking
          this.dataloading = false;
        }
      })
  }

  // function to remove favourite from list on button click
  removeFromFavourite(data: UserFavouriteSongList){
    this.dataloading = true;
    // pass the trackID, along with JWT token and username to API Service to delete track
    this.recomendedMusicService.deleteFavouriteList( this.sessionService.getAuthenticationSession()!,data.trackId, data.userId.username.toString())
      .subscribe({
        next: (data) => {
          //ig there is such record, it will be deleted and alret the user that the track have been deleted!
          alert(`The following Track have been removed from favourite list! \nTrack Name: ${data.trackName}. \nArtist Name: ${data.artistName}`);
          //will re render the favourite song list page and update the content taken from db
          this.getFavouriteMusic();
        }, error: ((err: HttpErrorResponse) => {
          //if there is no such track in their favoruite list will throw this alert
          if(err.status === 404){
            alert('There is no such song in favourite list!');
            console.log(`Song not found in favourite list! \nSong detail: \n${data}`);
          }
          //will throw this alert if something went wrong
          else{
            alert('Error has occur!');
            console.log(`Error had occur: ${err}`);
          }
          this.getFavouriteMusic();
        })
      })
  }
    // pass data to parent (welcome page)
    getTrackData(data: UserFavouriteSongList){
      // formate UserFavouriteSongList to spotifySong (Diff is only no username)
      const formatedData = {
        id: data.trackId,
        trackName: data.trackName,
        trackImageUrl: data.trackImageUrl,
        artistName: data.artistName
      } as spotifySong;
      
      this.getTrackDetail.emit({
        trackDetail: formatedData,
        backButtonPress: true,
        pageIndex: 2
      });
    }

}
