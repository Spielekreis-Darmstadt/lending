import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllLendIdentityCardsComponent } from './show-all-lend-identity-cards.component';

describe('ShowAllLendIdentityCardsComponent', () => {
  let component: ShowAllLendIdentityCardsComponent;
  let fixture: ComponentFixture<ShowAllLendIdentityCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowAllLendIdentityCardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAllLendIdentityCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
