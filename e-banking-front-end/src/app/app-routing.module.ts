import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from './customers/customers.component';
import {AccountsComponent} from './accounts/accounts.component';
import {NewCustomerComponent} from './new-customer/new-customer.component';
import {LoginComponent} from './login/login.component';
import {NavbarComponent} from './navbar/navbar.component';
import {AdminTemplateComponent} from './admin-template/admin-template.component';
import {authenticationGuard} from './guards/authentication.guard';
import {NotAuthorizedComponent} from './not-authorized/not-authorized.component';
import {authorizationGuard} from './guards/authorization.guard';
import {OperationsComponent} from './operations/operations.component';

const routes: Routes = [
  { path: "", redirectTo: "login", pathMatch: "full" },
  { path: "login", component: LoginComponent },
  { path: "not-authorized", component: NotAuthorizedComponent },
  {
    path: "admin",
    component: AdminTemplateComponent,
    canActivate: [authenticationGuard],
    children: [
      { path: "", redirectTo: "customers", pathMatch: "full" },
      { path: "customers", component: CustomersComponent },
      { path: "accounts", component: AccountsComponent },
      { path: "new-customer", component: NewCustomerComponent, canActivate: [authorizationGuard], data: { role: "ADMIN" } },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
