import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CustomerService} from '../services/customer.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { Customer } from '../model/customer.model';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-new-customer',
  standalone: false,
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit {
  newCustomerFormGroup!:FormGroup;
  isEditMode: boolean = false;
  constructor(private fb:FormBuilder,private customerService:CustomerService,  private _snackBar: MatSnackBar,@Inject(MAT_DIALOG_DATA) public data: Customer) { }

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      id: [this.data?.id || null],
      name: [this.data?.name || '', [Validators.required, Validators.minLength(4)]],
      email: [this.data?.email || '', [Validators.required, Validators.email]]
    });

    this.isEditMode = !!this.data?.id;
  }

  handleSaveCustomer() {
    const customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next: (data) => {
        this._snackBar.open("Customer "+ (this.isEditMode?"Updated":"Created") +" Successfully", "close", {
          duration: 3000,
          verticalPosition:'top',
          panelClass: ['success-snackbar','snackbar-below-navbar'],
        });
        this.newCustomerFormGroup.reset();
      },
      error: (err) => {
        this._snackBar.open("Error while "+ (this.isEditMode?"updating":"creating") +" customer", "Close", {
          duration: 3000,
          verticalPosition: 'top',
          panelClass: ['error-snackbar','snackbar-below-navbar'],

        });
        console.error(err);
      }
    });
  }
}
