import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ColumnAssignmentStepComponent } from './column-assignment-step.component';

describe('ColumnAssignmentStepComponent', () => {
  let component: ColumnAssignmentStepComponent;
  let fixture: ComponentFixture<ColumnAssignmentStepComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ColumnAssignmentStepComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ColumnAssignmentStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
