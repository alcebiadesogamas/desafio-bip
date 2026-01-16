import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BeneficioTransfer } from './beneficio-transfer';

describe('BeneficioTransfer', () => {
  let component: BeneficioTransfer;
  let fixture: ComponentFixture<BeneficioTransfer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BeneficioTransfer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BeneficioTransfer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
