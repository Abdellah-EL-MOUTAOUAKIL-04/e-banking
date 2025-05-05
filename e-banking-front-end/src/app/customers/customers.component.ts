import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CustomerService} from '../services/customer.service';
import {Observable} from 'rxjs';
import {Customer} from '../model/customer.model';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatDialog} from '@angular/material/dialog';
import {DeleteCustomerComponent} from '../delete-customer/delete-customer.component';
import {NewCustomerComponent} from '../new-customer/new-customer.component';
import {LoadingService} from '../services/loading.service';

@Component({
  selector: 'app-customers',
  standalone: false,
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{
  customers!:Array<Customer>;
  searchFormGroup!:FormGroup;
  errorMessage!:string;
  displayedColumns=['id', 'name','email','actions'];
  isLoading$!: Observable<boolean>;

  constructor(private customerService:CustomerService,private fb:FormBuilder,private _dialog:MatDialog,  private loadingService: LoadingService) {
  }
  ngOnInit(): void {
    this.isLoading$ = this.loadingService.isLoading$;
    this.searchFormGroup=this.fb.group({
      keyword:this.fb.control(null)
    });
    this.loadCustomers();
    this.customerService.customersUpdated$.subscribe(() => {
      this.loadCustomers();
    });
  }

  loadCustomers() {
    this.loadingService.show();
    this.customerService.getCustomers().subscribe({
      next: (data) => {
        this.customers = data;
        this.errorMessage = '';
        this.loadingService.hide();
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loadingService.hide();
      }
    });
  }

  handleSearchCustomers() {
    this.loadingService.show();
    let keyword = this.searchFormGroup?.value.keyword;
    this.customerService.searchCustomers(keyword).subscribe({
      next: (data) => {
        this.customers = data;
        this.errorMessage = data.length === 0 ? 'No customers found' : '';
        this.loadingService.hide();
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loadingService.hide();
      }
    });
  }

  handleEditCustomer(customer:Customer) {
    this._dialog.open(NewCustomerComponent, {
      data: customer
    }).afterClosed().subscribe(result => {

    });
  }
  handleDeleteCustomer(id:number) {
    this._dialog.open(DeleteCustomerComponent, {
      data: {id: id}
    }).afterClosed().subscribe(result => {
      if(result) {
        this.customerService.deleteCustomer(id).subscribe({
          next: () => {
            this.loadCustomers();
          },
          error: (err) => {
            this.errorMessage = err.message;
          }
        });
      }
    });
  }

  protected readonly console = console;
}
