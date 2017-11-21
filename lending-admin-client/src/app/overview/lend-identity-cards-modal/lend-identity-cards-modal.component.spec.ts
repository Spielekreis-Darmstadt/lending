import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LendIdentityCardsModalComponent } from './lend-identity-cards-modal.component';

describe('LendIdentityCardsModalComponent', () => {
  let component: LendIdentityCardsModalComponent;
  let fixture: ComponentFixture<LendIdentityCardsModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LendIdentityCardsModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LendIdentityCardsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
