import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MusicAppAPIService } from '../Service/music-app-api.service';

import { SearchMusicComponent } from './search-music.component';

describe('SearchMusicComponent', () => {
  let component: SearchMusicComponent;
  let fixture: ComponentFixture<SearchMusicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchMusicComponent],
      providers: [MusicAppAPIService]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SearchMusicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
