import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmNewGamesStepComponent } from './confirm-new-games-step.component';

describe('ConfirmNewGamesStepComponent', () => {
  let component: ConfirmNewGamesStepComponent;
  let fixture: ComponentFixture<ConfirmNewGamesStepComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmNewGamesStepComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmNewGamesStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
