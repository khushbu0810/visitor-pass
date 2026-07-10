import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuardDashboard } from './guard-dashboard';

describe('GuardDashboard', () => {
  let component: GuardDashboard;
  let fixture: ComponentFixture<GuardDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuardDashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(GuardDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
