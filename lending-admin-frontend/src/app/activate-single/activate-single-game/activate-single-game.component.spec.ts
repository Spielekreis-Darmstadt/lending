import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateSingleGameComponent } from './activate-single-game.component';

describe('ActivateSingleGameComponent', () => {
  let component: ActivateSingleGameComponent;
  let fixture: ComponentFixture<ActivateSingleGameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActivateSingleGameComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivateSingleGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
