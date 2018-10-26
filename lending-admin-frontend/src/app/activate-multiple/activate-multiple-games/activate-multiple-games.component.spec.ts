import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateMultipleGamesComponent } from './activate-multiple-games.component';

describe('ActivateMultipleGamesComponent', () => {
  let component: ActivateMultipleGamesComponent;
  let fixture: ComponentFixture<ActivateMultipleGamesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActivateMultipleGamesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivateMultipleGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
