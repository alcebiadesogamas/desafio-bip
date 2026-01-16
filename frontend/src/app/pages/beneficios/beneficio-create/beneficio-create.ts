import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Beneficio } from '../../../core/models/beneficio.model';
import { BeneficioService } from '../../../core/services/beneficio.service';

@Component({
  selector: 'app-beneficio-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficio-create.html',
  styleUrl: './beneficio-create.scss',
})
export class BeneficioCreate {
  form: FormGroup;
  loading = signal(false);
  error = signal<string | null>(null);
  success = signal(false);

  constructor(
    private fb: FormBuilder,
    private service: BeneficioService,
    private router: Router
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      value: [0, [Validators.required, Validators.min(0.01)]],
      active: [true]
    });
  }

  get nameControl() {
    return this.form.get('name');
  }

  get descriptionControl() {
    return this.form.get('description');
  }

  get valueControl() {
    return this.form.get('value');
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    const beneficio: Partial<Beneficio> = this.form.value;

    this.service.create(beneficio).subscribe({
      next: () => {
        this.success.set(true);
        setTimeout(() => {
          this.router.navigate(['/beneficios']);
        }, 1500);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set('Erro ao criar benefício. Tente novamente.');
        console.error('Erro:', err);
      }
    });
  }

  cancel() {
    this.router.navigate(['/']);
  }

  hasError(controlName: string): boolean {
    const control = this.form.get(controlName);
    return !!(control && control.invalid && control.touched);
  }

  getErrorMessage(controlName: string): string {
    const control = this.form.get(controlName);
    if (!control || !control.errors) return '';

    if (control.errors['required']) {
      return 'Este campo é obrigatório';
    }
    if (control.errors['minlength']) {
      const min = control.errors['minlength'].requiredLength;
      return `Mínimo de ${min} caracteres`;
    }
    if (control.errors['min']) {
      return 'O valor deve ser maior que zero';
    }
    return '';
  }
}