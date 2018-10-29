import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMultipleEnvelopesComponent } from './add-multiple-envelopes.component';

describe('AddMultipleEnvelopesComponent', () => {
  let component: AddMultipleEnvelopesComponent;
  let fixture: ComponentFixture<AddMultipleEnvelopesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddMultipleEnvelopesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMultipleEnvelopesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
