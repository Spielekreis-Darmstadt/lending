import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMultipleIdentityCardsComponent } from './add-multiple-identity-cards.component';

describe('AddMultipleIdentityCardsComponent', () => {
  let component: AddMultipleIdentityCardsComponent;
  let fixture: ComponentFixture<AddMultipleIdentityCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddMultipleIdentityCardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMultipleIdentityCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
