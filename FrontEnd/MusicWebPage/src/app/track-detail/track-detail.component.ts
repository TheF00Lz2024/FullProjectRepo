import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { showTrackDetail } from '../Modal/spotifyInterface';

@Component({
  selector: 'app-track-detail',
  standalone: true,
  imports: [MatButtonModule],
  templateUrl: './track-detail.component.html',
  styleUrl: './track-detail.component.css'
})
export class TrackDetailComponent {
  @Input() trackDetailData!: showTrackDetail;
  
  //same as sibling component (sibling component are recommended track, search track and favorutie track)
  //(emit event to parent component welcome page)
  @Output() getTrackDetail = new EventEmitter<showTrackDetail>();

  //back button function to redirec the user back to the previous page
  backFunction(data: showTrackDetail){
    this.getTrackDetail.emit({
      trackDetail: data.trackDetail,
      backButtonPress: false,
      pageIndex: data.pageIndex
    });  
  }
}
