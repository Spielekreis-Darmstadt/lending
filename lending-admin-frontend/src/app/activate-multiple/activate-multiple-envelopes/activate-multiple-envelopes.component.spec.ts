import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateMultipleEnvelopesComponent } from './activate-multiple-envelopes.component';

describe('ActivateMultipleEnvelopesComponent', () => {
  let component: ActivateMultipleEnvelopesComponent;
  let fixture: ComponentFixture<ActivateMultipleEnvelopesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActivateMultipleEnvelopesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivateMultipleEnvelopesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
