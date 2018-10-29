import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllIdentityCardsComponent } from './show-all-identity-cards.component';

describe('ShowAllIdentityCardsComponent', () => {
  let component: ShowAllIdentityCardsComponent;
  let fixture: ComponentFixture<ShowAllIdentityCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowAllIdentityCardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAllIdentityCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
