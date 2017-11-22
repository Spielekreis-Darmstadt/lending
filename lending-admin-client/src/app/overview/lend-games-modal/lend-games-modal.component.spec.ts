import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LendGamesModalComponent } from './lend-games-modal.component';

describe('LendGamesModalComponent', () => {
  let component: LendGamesModalComponent;
  let fixture: ComponentFixture<LendGamesModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LendGamesModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LendGamesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
