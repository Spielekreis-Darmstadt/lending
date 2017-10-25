import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckSheetContentStepComponent } from './check-sheet-content-step.component';

describe('CheckSheetContentStepComponent', () => {
  let component: CheckSheetContentStepComponent;
  let fixture: ComponentFixture<CheckSheetContentStepComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckSheetContentStepComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckSheetContentStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
