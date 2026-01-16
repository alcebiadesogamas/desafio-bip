import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BeneficioCreate } from './beneficio-create';

describe('BeneficioCreate', () => {
  let component: BeneficioCreate;
  let fixture: ComponentFixture<BeneficioCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BeneficioCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BeneficioCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
