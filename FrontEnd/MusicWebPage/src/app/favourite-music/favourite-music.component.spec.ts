import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MusicAppAPIService } from '../Service/music-app-api.service';

import { FavouriteMusicComponent } from './favourite-music.component';

describe('FavouriteMusicComponent', () => {
  let component: FavouriteMusicComponent;
  let fixture: ComponentFixture<FavouriteMusicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FavouriteMusicComponent, MusicAppAPIService]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FavouriteMusicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
