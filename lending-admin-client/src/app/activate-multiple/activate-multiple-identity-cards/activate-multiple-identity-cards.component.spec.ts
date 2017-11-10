import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateMultipleIdentityCardsComponent } from './activate-multiple-identity-cards.component';

describe('ActivateMultipleIdentityCardsComponent', () => {
  let component: ActivateMultipleIdentityCardsComponent;
  let fixture: ComponentFixture<ActivateMultipleIdentityCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActivateMultipleIdentityCardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivateMultipleIdentityCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
