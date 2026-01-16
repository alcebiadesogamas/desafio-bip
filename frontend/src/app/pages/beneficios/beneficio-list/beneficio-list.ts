import { Component, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Beneficio } from '../../../core/models/beneficio.model';
import { BeneficioService } from '../../../core/services/beneficio.service';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './beneficio-list.html',
  styleUrls: ['./beneficio-list.scss'],
})
export class BeneficioList implements OnInit {
  beneficios = signal<Beneficio[]>([]);
  loading = signal(true);
  
  showDeleteModal = signal(false);
  beneficioToDelete = signal<Beneficio | null>(null);
  deleting = signal(false);
  
  message = signal<string | null>(null);
  messageType = signal<'success' | 'error'>('success');

  constructor(
    private readonly service: BeneficioService,
    private readonly cdr: ChangeDetectorRef,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadBeneficios();
  }

  loadBeneficios(): void {
    this.loading.set(true);
    this.service.findAll().subscribe({
      next: (data) => {
        this.beneficios.set(data);
        this.loading.set(false);
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.loading.set(false);
        this.showMessage('Erro ao carregar benefícios', 'error');
      }
    });
  }

  onEdit(id: number): void {
    this.router.navigate(['/beneficios/editar', id]);
  }

  onDeleteClick(beneficio: Beneficio): void {
    this.beneficioToDelete.set(beneficio);
    this.showDeleteModal.set(true);
  }

  confirmDelete(): void {
    const beneficio = this.beneficioToDelete();
    if (!beneficio) return;

    this.deleting.set(true);

    this.service.delete(beneficio.id).subscribe({
      next: () => {
        this.showMessage('Benefício excluído com sucesso!', 'success');
        this.closeDeleteModal();
        this.loadBeneficios();
      },
      error: (error) => {
        console.error('Erro ao excluir benefício:', error);
        this.showMessage('Erro ao excluir benefício. Tente novamente.', 'error');
        this.deleting.set(false);
      }
    });
  }

  closeDeleteModal(): void {
    this.showDeleteModal.set(false);
    this.beneficioToDelete.set(null);
    this.deleting.set(false);
  }

  showMessage(msg: string, type: 'success' | 'error'): void {
    this.message.set(msg);
    this.messageType.set(type);
    
    setTimeout(() => {
      this.message.set(null);
    }, 5000);
  }

  navigateToNew(): void {
    this.router.navigate(['/beneficios/novo']);
  }

  navigateToTransfer(): void {
    this.router.navigate(['/beneficios/transferir']);
  }
}