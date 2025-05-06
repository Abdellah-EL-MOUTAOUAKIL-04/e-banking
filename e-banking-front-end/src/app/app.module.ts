import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import {MatToolbar, MatToolbarModule} from '@angular/material/toolbar';
import {MatButton, MatButtonModule, MatIconButton} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatDrawerContainer, MatSidenavModule} from '@angular/material/sidenav';
import {MatListItem, MatNavList} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import {HttpClientModule, provideHttpClient, withInterceptors} from '@angular/common/http';
import {MatCard, MatCardModule} from '@angular/material/card';
import {MatTable, MatTableModule} from '@angular/material/table';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInput, MatInputModule} from '@angular/material/input';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogModule,
  MatDialogTitle
} from '@angular/material/dialog';
import { DeleteCustomerComponent } from './delete-customer/delete-customer.component';
import { LoginComponent } from './login/login.component';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import {appHttpInterceptor} from './interceptors/app-http.interceptor';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';
import {MatChip, MatChipsModule} from '@angular/material/chips';
import { OperationsComponent } from './operations/operations.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSortModule} from '@angular/material/sort';
import { NewOperationComponent } from './new-operation/new-operation.component';
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle
} from '@angular/material/datepicker';
import {MatSelectModule} from '@angular/material/select';
import {MatOptionModule} from '@angular/material/core';
import {MatMomentDateModule} from '@angular/material-moment-adapter';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CustomersComponent,
    AccountsComponent,
    NewCustomerComponent,
    DeleteCustomerComponent,
    LoginComponent,
    AdminTemplateComponent,
    NotAuthorizedComponent,
    LoadingSpinnerComponent,
    OperationsComponent,
    NewOperationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatToolbarModule,
    MatIconModule,
    MatIconButton,
    MatButtonModule,
    MatMenuModule,
    MatSidenavModule,
    MatListItem,
    MatNavList,
    MatCardModule,
    MatTableModule,
    MatProgressSpinnerModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    FormsModule,
    MatChipsModule,
    MatPaginatorModule,
    MatSortModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatSelectModule,
    MatOptionModule,
  ],
  providers: [
    provideHttpClient(withInterceptors([appHttpInterceptor]))
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
