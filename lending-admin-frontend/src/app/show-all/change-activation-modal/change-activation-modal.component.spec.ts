import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeActivationModalComponent } from './change-activation-modal.component';

describe('ChangeActivationModalComponent', () => {
  let component: ChangeActivationModalComponent;
  let fixture: ComponentFixture<ChangeActivationModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangeActivationModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeActivationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
