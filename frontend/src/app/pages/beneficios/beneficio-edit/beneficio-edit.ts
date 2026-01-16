import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { BeneficioService } from '../../../core/services/beneficio.service';
import { Beneficio } from '../../../core/models/beneficio.model';

@Component({
  selector: 'app-beneficio-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficio-edit.html',
  styleUrls: ['./beneficio-edit.scss'],
})
export class BeneficioEdit implements OnInit {
  form: FormGroup;
  loading = signal(false);
  loadingData = signal(true);
  error = signal<string | null>(null);
  success = signal(false);
  beneficioId!: number;

  constructor(
    private fb: FormBuilder,
    private service: BeneficioService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      value: [0, [Validators.required, Validators.min(0.01)]],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.beneficioId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadBeneficio();
  }

  loadBeneficio(): void {
    this.service.findById(this.beneficioId).subscribe({
      next: (beneficio) => {
        this.form.patchValue(beneficio);
        this.loadingData.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar benefício:', err);
        this.error.set('Erro ao carregar benefício. Redirecionando...');
        this.loadingData.set(false);
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 2000);
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    const beneficio: Partial<Beneficio> = this.form.value;
    beneficio.id = this.beneficioId;

    this.service.update(beneficio).subscribe({
      next: () => {
        this.success.set(true);
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 1500);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set('Erro ao atualizar benefício. Tente novamente.');
        console.error('Erro:', err);
      }
    });
  }

  cancel(): void {
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