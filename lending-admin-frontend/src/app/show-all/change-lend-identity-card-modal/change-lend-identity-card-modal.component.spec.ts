import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeLendIdentityCardModalComponent } from './change-lend-identity-card-modal.component';

describe('ChangeLendIdentityCardModalComponent', () => {
  let component: ChangeLendIdentityCardModalComponent;
  let fixture: ComponentFixture<ChangeLendIdentityCardModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangeLendIdentityCardModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeLendIdentityCardModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
