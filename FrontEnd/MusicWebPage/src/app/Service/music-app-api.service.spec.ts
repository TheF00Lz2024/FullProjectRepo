import { TestBed } from '@angular/core/testing';

import { MusicAppAPIService } from './music-app-api.service';

describe('MusicAppAPIService', () => {
  let service: MusicAppAPIService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    });
    service = TestBed.inject(MusicAppAPIService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
