import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePass } from './create-pass';

describe('CreatePass', () => {
  let component: CreatePass;
  let fixture: ComponentFixture<CreatePass>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatePass],
    }).compileComponents();

    fixture = TestBed.createComponent(CreatePass);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
