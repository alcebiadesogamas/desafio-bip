import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Beneficio } from '../../../core/models/beneficio.model';
import { BeneficioService } from '../../../core/services/beneficio.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficio-transfer.html',
  styleUrls: ['./beneficio-transfer.scss'],
})
export class BeneficioTransfer implements OnInit {
  benefitFormGroup: FormGroup;
  beneficios = signal<Beneficio[]>([]);
  loading = signal(false);
  loadingBeneficios = signal(true);
  message = signal<string | null>(null);
  messageType = signal<'success' | 'error'>('success');

  beneficioOrigem = signal<Beneficio | null>(null);
  beneficioDestino = signal<Beneficio | null>(null);

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: BeneficioService,
    private readonly router: Router
  ) {
    this.benefitFormGroup = this.fb.group({
      fromId: ['', [Validators.required]],
      toId: ['', [Validators.required]],
      amount: [0, [Validators.required, Validators.min(0.01)]]
    });

    this.benefitFormGroup.get('fromId')?.valueChanges.subscribe(id => {
      this.beneficioOrigem.set(
        this.beneficios().find(b => b.id === Number(id)) || null
      );
    });

    this.benefitFormGroup.get('toId')?.valueChanges.subscribe(id => {
      this.beneficioDestino.set(
        this.beneficios().find(b => b.id === Number(id)) || null
      );
    });
  }

  ngOnInit(): void {
    this.loadBeneficios();
  }

  loadBeneficios(): void {
    this.service.findAll().subscribe({
      next: (data) => {
        this.beneficios.set(data.filter(b => b.active));
        this.loadingBeneficios.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar benefícios:', err);
        this.loadingBeneficios.set(false);
        this.showMessage('Erro ao carregar benefícios', 'error');
      }
    });
  }

  transfer(): void {
    if (this.benefitFormGroup.invalid) {
      this.benefitFormGroup.markAllAsTouched();
      return;
    }

    const { fromId, toId, amount } = this.benefitFormGroup.value;

    if (fromId === toId) {
      this.showMessage('Origem e destino não podem ser iguais', 'error');
      return;
    }

    this.loading.set(true);
    this.message.set(null);

    this.service.transfer(Number(fromId), Number(toId), Number(amount)).subscribe({
      next: () => {
        this.showMessage('Transferência realizada com sucesso!', 'success');
        this.benefitFormGroup.reset();
        this.beneficioOrigem.set(null);
        this.beneficioDestino.set(null);
        this.loadBeneficios(); 
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.showMessage(
          err.error?.message || 'Erro ao realizar transferência. Tente novamente.',
          'error'
        );
        console.error('Erro:', err);
      }
    });
  }

  showMessage(msg: string, type: 'success' | 'error'): void {
    this.message.set(msg);
    this.messageType.set(type);

    setTimeout(() => {
      this.message.set(null);
    }, 5000);
  }

  hasError(controlName: string): boolean {
    const control = this.benefitFormGroup.get(controlName);
    return !!(control && control.invalid && control.touched);
  }

  getErrorMessage(controlName: string): string {
    const control = this.benefitFormGroup.get(controlName);
    if (!control || !control.errors) return '';

    if (control.errors['required']) return 'Este campo é obrigatório';
    if (control.errors['min']) return 'O valor deve ser maior que zero';

    return '';
  }

  cancel(): void {
    this.router.navigate(['/']);
  }
}