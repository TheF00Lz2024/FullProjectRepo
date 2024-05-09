import { CommonModule } from '@angular/common';
import { Component, ViewChild} from '@angular/core';
import {MatTabChangeEvent, MatTabsModule} from '@angular/material/tabs';
import { FavouriteMusicComponent } from '../favourite-music/favourite-music.component';
import { showTrackDetail } from '../Modal/spotifyInterface';
import { RecommendedMusicComponent } from '../recommended-music/recommended-music.component';
import { SearchMusicComponent } from '../search-music/search-music.component';
import { TrackDetailComponent } from '../track-detail/track-detail.component';



@Component({
  selector: 'app-welcome-page',
  standalone: true,
  imports: [MatTabsModule, RecommendedMusicComponent, SearchMusicComponent, FavouriteMusicComponent, TrackDetailComponent, CommonModule],
  templateUrl: './welcome-page.component.html',
  styleUrl: './welcome-page.component.css'
})
export class WelcomePageComponent{

  @ViewChild(RecommendedMusicComponent)
  private recommendedMusicComponent!: RecommendedMusicComponent;

  @ViewChild(SearchMusicComponent)
  private searchMusicComponent!: SearchMusicComponent;

  @ViewChild(FavouriteMusicComponent)
  private favouriteMusicComponent!: FavouriteMusicComponent;

  // for showing track detail
  showTrackDetail: boolean = false;
  trackDetailData = {} as showTrackDetail;

  // listening to the vent if the tab button have been click and fire the component event
  // ref from https://stackoverflow.com/questions/49629712/how-to-reset-refresh-tab-body-data-in-angular-material-if-user-move-from-one-tab
  tabEvent(event:MatTabChangeEvent){
    this.showTrackDetail = false;
    if(event.index == 0){
      this.recommendedMusicComponent.getRecommendedMusic();
    }else if(event.index == 1){
      this.searchMusicComponent.reinitializeForm();
    }else{
      this.favouriteMusicComponent.getFavouriteMusic();
    }
  }

  //pass data to track detail component to render track detail to user / passing track detail data to parent to notify redirect to whcih tab when back button click
  trackDetail(data: showTrackDetail){
    this.showTrackDetail = data.backButtonPress;
    this.trackDetailData = {
      trackDetail: data.trackDetail,
      backButtonPress: data.backButtonPress,
      pageIndex: data.pageIndex
    } as showTrackDetail
  }


}

