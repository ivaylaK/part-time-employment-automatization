import { Routes } from '@angular/router';
import {DashboardAdminComponent} from './components/admin/dashboard-admin/dashboard-admin.component';
import {ApplicationFormComponent} from './components/user/application-form/application-form.component';
import {JobDetailsAdminComponent} from './components/admin/job-details-admin/job-details-admin.component';
import {JobCreateEditComponent, JobCreateEditMode} from './components/admin/job-create-edit/job-create-edit.component';
import {ApplicationGuard} from './guard/application.guard';
import {AdminGuard} from './guard/admin.guard';
import {LoginComponent} from './components/login/login.component';
import {DashboardUserComponent} from './components/user/dashboard-user/dashboard-user.component';
import {ApplicantsComponent} from './components/admin/applicants/applicants.component';
import {JobDetailsUserComponent} from './components/user/job-details-user/job-details-user.component';
import {LoginGuard} from './guard/login.guard';
import {JobApplicationsComponent} from './components/admin/job-applications/job-applications.component';

export const routes: Routes = [

  { path: 'admin/jobs', component: DashboardAdminComponent, canActivate: [AdminGuard] },
  { path: 'admin/jobs/create', component: JobCreateEditComponent, data: { mode: JobCreateEditMode.create }, canActivate: [AdminGuard] },
  { path: 'admin/jobs/edit/:id', component: JobCreateEditComponent, data: { mode: JobCreateEditMode.edit }, canActivate: [AdminGuard] },
  { path: 'admin/jobs/details/:id', component: JobDetailsAdminComponent },

  { path: 'admin/applicants', component: ApplicantsComponent, canActivate: [AdminGuard] },
  { path: 'admin/job-applications', component: JobApplicationsComponent, canActivate: [AdminGuard] },

  { path: 'dashboard', component: DashboardUserComponent },
  { path: 'dashboard/details/:id', component: JobDetailsUserComponent },
  { path: 'apply/job/:id', component: ApplicationFormComponent, canActivate: [LoginGuard] },

  { path: 'apply', component: ApplicationFormComponent, canActivate: [ApplicationGuard] },
  { path: 'authentication/login', component: LoginComponent },
  {
    path: '**', redirectTo: 'authentication/login'
  }
];
