import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllGamesComponent } from './show-all-games.component';

describe('ShowAllGamesComponent', () => {
  let component: ShowAllGamesComponent;
  let fixture: ComponentFixture<ShowAllGamesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowAllGamesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAllGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
