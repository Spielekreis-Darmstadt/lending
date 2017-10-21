import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileSelectionStepComponent } from './file-selection-step.component';

describe('FileSelectionStepComponent', () => {
  let component: FileSelectionStepComponent;
  let fixture: ComponentFixture<FileSelectionStepComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileSelectionStepComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileSelectionStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
