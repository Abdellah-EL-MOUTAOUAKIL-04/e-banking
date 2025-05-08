import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import { MatButtonModule, MatIconButton} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { MatSidenavModule} from '@angular/material/sidenav';
import { MatListItem, MatListModule, MatNavList} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import {HttpClientModule, provideHttpClient, withInterceptors} from '@angular/common/http';
import { MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule} from '@angular/material/input';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import {
  MatDialogModule,
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
  MatDatepickerModule,
} from '@angular/material/datepicker';
import {MatSelectModule} from '@angular/material/select';
import {MatOptionModule} from '@angular/material/core';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import { HomeComponent } from './home/home.component';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatDividerModule} from '@angular/material/divider';
import {BaseChartDirective, provideCharts, withDefaultRegisterables} from 'ng2-charts';

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
    NewOperationComponent,
    HomeComponent
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
    MatGridListModule,
    MatDividerModule,
    MatListModule,
    BaseChartDirective,
  ],
  providers: [
    provideHttpClient(withInterceptors([appHttpInterceptor])),
    provideCharts(withDefaultRegisterables())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
