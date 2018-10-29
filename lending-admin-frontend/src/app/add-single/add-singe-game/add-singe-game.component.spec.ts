import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSingleGameComponent } from './add-single-game.component';

describe('AddSingleGameComponent', () => {
  let component: AddSingleGameComponent;
  let fixture: ComponentFixture<AddSingleGameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddSingleGameComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddSingleGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
