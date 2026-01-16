import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BeneficioEdit } from './beneficio-edit';

describe('BeneficioEdit', () => {
  let component: BeneficioEdit;
  let fixture: ComponentFixture<BeneficioEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BeneficioEdit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BeneficioEdit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
