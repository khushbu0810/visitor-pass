import { Routes } from '@angular/router';
import { SignUp } from './components/sign-up/sign-up';
import { Login } from './components/login/login';
import { GuardDashboard } from './components/guard-dashboard/guard-dashboard';
import { ResidentProfile } from './components/resident-profile/resident-profile';
import { EditResident } from './components/edit-resident/edit-resident';
import { AuthGuard } from './guards/auth-guard-guard';
import { AddVisitor } from './components/add-visitor/add-visitor';
import { CreatePass } from './components/create-pass/create-pass';
import { ScanQr } from './components/scan-qr/scan-qr';

export const routes: Routes = [
    { path: '', component: Login },
    { path: 'signup', component: SignUp },
    { path: 'login', component: Login },

    { path: 'resident-profile', component: ResidentProfile, canActivate: [AuthGuard] },
    { path: 'edit-resident/:id', component: EditResident, canActivate: [AuthGuard] },

    { path: 'guard-dashboard', component: GuardDashboard, canActivate: [AuthGuard] },

    { path: 'add-visitor', component: AddVisitor, canActivate: [AuthGuard] },

    { path: 'create-pass/:id', component: CreatePass, canActivate: [AuthGuard] },
    { path: 'scan-qr', component: ScanQr, canActivate: [AuthGuard] }
];
