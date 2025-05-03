import {Component, inject, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {NewCustomerComponent} from '../new-customer/new-customer.component';
import {CustomerService} from '../services/customer.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  standalone: false,
})
export class NavbarComponent implements OnInit {
  constructor(private _dialog:MatDialog,private customerService:CustomerService) { }

  ngOnInit(): void {
    // Initialization logic can go here
  }

  openNewCustomerDialog(){
      const dialogRef = this._dialog.open(NewCustomerComponent, {
      });

      dialogRef.afterClosed().subscribe(result => {
      });
  }

}
