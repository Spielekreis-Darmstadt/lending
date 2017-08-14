import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMultipleGamesComponent } from './add-multiple-games.component';

describe('AddMultipleGamesComponent', () => {
  let component: AddMultipleGamesComponent;
  let fixture: ComponentFixture<AddMultipleGamesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddMultipleGamesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMultipleGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
