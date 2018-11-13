import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllLendGamesComponent } from './show-all-lend-games.component';

describe('ShowAllLendGamesComponent', () => {
  let component: ShowAllLendGamesComponent;
  let fixture: ComponentFixture<ShowAllLendGamesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowAllLendGamesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAllLendGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
