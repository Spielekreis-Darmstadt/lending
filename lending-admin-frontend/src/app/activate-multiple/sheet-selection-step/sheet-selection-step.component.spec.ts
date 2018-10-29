import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SheetSelectionStepComponent } from './sheet-selection-step.component';

describe('SheetSelectionStepComponent', () => {
  let component: SheetSelectionStepComponent;
  let fixture: ComponentFixture<SheetSelectionStepComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SheetSelectionStepComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SheetSelectionStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
