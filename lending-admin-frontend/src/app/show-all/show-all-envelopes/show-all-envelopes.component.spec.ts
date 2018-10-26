import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllEnvelopesComponent } from './show-all-envelopes.component';

describe('ShowAllEnvelopesComponent', () => {
  let component: ShowAllEnvelopesComponent;
  let fixture: ComponentFixture<ShowAllEnvelopesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowAllEnvelopesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAllEnvelopesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
