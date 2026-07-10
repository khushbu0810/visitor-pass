import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResidentProfile } from './resident-profile';

describe('ResidentProfile', () => {
  let component: ResidentProfile;
  let fixture: ComponentFixture<ResidentProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResidentProfile],
    }).compileComponents();

    fixture = TestBed.createComponent(ResidentProfile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
