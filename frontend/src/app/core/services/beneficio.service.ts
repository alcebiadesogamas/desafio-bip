import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Beneficio } from '../models/beneficio.model';

@Injectable({ providedIn: 'root' })
export class BeneficioService {
  private readonly api = 'http://localhost:8080/api/v1/beneficios';

  constructor(private readonly http: HttpClient) { }

  findAll(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.api);
  }

  findById(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.api}/${id}`);
  }

  create(beneficio: Partial<Beneficio>): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.api, beneficio);
  }

  update(beneficio: Partial<Beneficio>): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.api}`, beneficio);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  transfer(fromId: number, toId: number, amount: number): Observable<void> {
    return this.http.post<void>(`${this.api}/transfer`, {
      fromId,
      toId,
      amount
    });
  }
}