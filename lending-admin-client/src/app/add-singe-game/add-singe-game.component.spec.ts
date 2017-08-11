import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSingeGameComponent } from './add-singe-game.component';

describe('AddSingeGameComponent', () => {
  let component: AddSingeGameComponent;
  let fixture: ComponentFixture<AddSingeGameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddSingeGameComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddSingeGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
