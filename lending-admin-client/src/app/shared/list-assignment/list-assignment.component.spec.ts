import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListAssignmentComponent } from './list-assignment.component';

describe('ListAssignmentComponent', () => {
  let component: ListAssignmentComponent;
  let fixture: ComponentFixture<ListAssignmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListAssignmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
