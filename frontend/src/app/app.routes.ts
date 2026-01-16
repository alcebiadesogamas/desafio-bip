import { Routes } from '@angular/router';
import { BeneficioList } from './pages/beneficios/beneficio-list/beneficio-list';
import { BeneficioCreate } from './pages/beneficios/beneficio-create/beneficio-create';
import { BeneficioTransfer } from './pages/beneficios/beneficio-transfer/beneficio-transfer';

export const routes: Routes = [
    {
        path: '',
        component: BeneficioList,
    },
    {
        path: 'beneficios/novo',
        component: BeneficioCreate
    },
    {
        path: 'beneficios/transferir',
        component: BeneficioTransfer
    }
];
