import { Component, OnInit, signal } from '@angular/core';
import { Beneficio } from '../../../core/models/beneficio.model';
import { BeneficioService } from '../../../core/services/beneficio.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-beneficio-list',
    imports: [CommonModule, RouterLink],
    templateUrl: './beneficio-list.html',
    styleUrls: ['./beneficio-list.scss'],
})
export class BeneficioList implements OnInit {
    beneficios = signal<Beneficio[]>([]);
    loading = signal(true);

    constructor(private readonly service: BeneficioService) { }

    ngOnInit(): void {
        this.service.findAll().subscribe({
            next: (data) => {
                this.beneficios.set(data);
                this.loading.set(false);
            },
            error: (error) => {
                this.loading.set(false);
            }
        });
    }
}
