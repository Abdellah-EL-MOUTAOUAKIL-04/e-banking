import {Component, inject, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {NewCustomerComponent} from '../new-customer/new-customer.component';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {NewOperationComponent} from '../new-operation/new-operation.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  standalone: false,
})
export class NavbarComponent implements OnInit {
  constructor(private _dialog:MatDialog,public authService:AuthService,private router:Router) { }

  ngOnInit(): void {
    // Initialization logic can go here
  }

  openNewCustomerDialog(){
      const dialogRef = this._dialog.open(NewCustomerComponent, {
      });

      dialogRef.afterClosed().subscribe(result => {
      });
  }
  handleLogout() {
    this.authService.logout();
  }

  openNewOperationDialog() {
    const dialogRef = this._dialog.open(NewOperationComponent, {
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
