import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, FormsModule, NgForm, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { mergeMap } from 'rxjs';
import { user } from '../Modal/apiResponse';
import { showTrackDetail, spotifySong, UserFavouriteSongList } from '../Modal/spotifyInterface';
import { MusicAppAPIService } from '../Service/music-app-api.service';
import { SessionServiceService } from '../Service/session-service.service';

/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-search-music',
  standalone: true,
  imports: [FormsModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule, CommonModule, MatButtonModule,
    MatTableModule, MatPaginatorModule, MatProgressSpinnerModule],
  templateUrl: './search-music.component.html',
  styleUrl: './search-music.component.css'
})
export class SearchMusicComponent{
  //from angular material authentication fo user input from form (email)
  matcher = new MyErrorStateMatcher();
  searchSong: FormGroup;
  dataSource = new MatTableDataSource<spotifySong>([]);
  dataloading: boolean = false;
  @Output() getTrackDetail = new EventEmitter<showTrackDetail>();

  // setting up form to search for data
  get songTitleControl(): FormControl {
    return this.searchSong.get('songTitle') as FormControl;
  }

  //create a constructor for calling API and Session Service and build a form using reactive form
  constructor(private spotifyApiService: MusicAppAPIService, private sessionService: SessionServiceService, private formBuilder: FormBuilder) {
    this.searchSong = this.formBuilder.group({
      songTitle: this.formBuilder.control('', [Validators.required])
    })
  }

  // table initialization
  displayedColumns: string[] = ['id', 'trackName', 'ArtistName', 'addToFavourite'];

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  //function once the button is press, searching of track title commence
  searchSongTitle() {
    this.dataloading = true;
    //call API service to generate Spotify token and passing JWT token 
    this.spotifyApiService.getSpotifyToken(this.sessionService.getAuthenticationSession()!)
      .pipe(mergeMap((data) => {
        //if there is no issue from SPotify and JWT Token is authenicated, pass the JWT token again, with the created Spotify created token, with track title
        return this.spotifyApiService.getSearchMusicTrack(this.sessionService.getAuthenticationSession()!,data.access_token, this.songTitleControl.value)
      })).subscribe({
        next: (data) => {
          //if there is no issue pass the data generated to table datasource
          this.dataSource = new MatTableDataSource<spotifySong>(data);
          this.dataSource.paginator = this.paginator;
          this.dataloading = false;
        }
      });
  }

  // function to add music to favourite
  addToFavourite(data: spotifySong) {
    this.dataloading = true;
    // create a user obj
    const newUser = {
      username: this.sessionService.getUsernameSession(),
      password: ''
    } as user;
    //create a ujserfavouritesonglist obj
    const newData = {
      trackId: data.id,
      userId: newUser,
      trackName: data.trackName,
      trackImageUrl: data.trackImageUrl,
      artistName: data.artistName
    } as UserFavouriteSongList;
    //pass the obj to API service and save the song to db
    this.spotifyApiService.addToFavouriteList(this.sessionService.getAuthenticationSession()!, newData)
      .subscribe({
        next: (data) => {
          //if there is no issue, will alert the user the song had been added
          alert(`Track have been added to favourite! \nTrack detail \nTrack Name: ${data.trackName} \nArtist Name: ${data.artistName}`);
          this.dataloading = false;
        }, error: ((err: HttpErrorResponse) => {
          //if this song is already in favourite track list will send this alert
          if (err.status === 403) {
            alert('This track already have been added to favourite track!')
            console.log('Duplicate track being added!');
            console.log(`Track detail: ${newData}`)
          } else {
            //if there is some unkown error happening
            alert('Error has occur!');
            console.log(`Error had occur: ${err}`);
          }
          this.dataloading = false;
        })
      })
  }

  //to reinitialize the form when the user change click on the current tab
  reinitializeForm() {
    this.dataloading = false;
    this.dataSource = new MatTableDataSource<spotifySong>([]);
    this.songTitleControl.setValue('');
  }

    // pass user selected track to parent(welcome-page)
    getTrackData(data: spotifySong){
      this.getTrackDetail.emit({
        trackDetail: data,
        backButtonPress: true,
        pageIndex: 1
      });
    }
}
