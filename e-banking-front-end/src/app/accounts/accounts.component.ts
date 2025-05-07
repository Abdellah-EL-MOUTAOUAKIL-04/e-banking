import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable} from 'rxjs';
import {LoadingService} from '../services/loading.service';
import {Account} from '../model/account.model';
import {AccountService} from '../services/account.service';
import {MatDialog} from '@angular/material/dialog';
import {OperationsComponent} from '../operations/operations.component';

@Component({
  selector: 'app-accounts',
  standalone: false,
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css',
})
export class AccountsComponent implements OnInit{
  accounts!:Array<Account>;
  searchFormGroup!:FormGroup;
  isLoading$!: Observable<boolean>;
  displayedColumns=['id', 'type','balance','createdAt','status','owner',"actions"];

  constructor(private _dialog:MatDialog,private fb:FormBuilder,private accountService:AccountService,private loadingService: LoadingService) {
  }
  ngOnInit(): void {
    this.isLoading$ = this.loadingService.isLoading$;
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control(null),
    });
    this.loadAccounts();
  }

  loadAccounts() {
      this.loadingService.show();
      this.accountService.getAccounts().subscribe({
        next: (data) => {
          this.accounts = data;
          this.loadingService.hide();
        },
        error: (err) => {
          console.error(err);
          this.loadingService.hide();
        }
      });
  }

  handleSearchAccount() {
    this.loadingService.show();
    let keyword = this.searchFormGroup?.value.keyword;
    this.accountService.searchAccounts(keyword).subscribe({
      next: (data) => {
        this.accounts = data;
        this.loadingService.hide();
      },
      error: (err) => {
        console.error(err);
        this.loadingService.hide();
      }
    });
  }

  openOperationsDialog(id:string) {
    const dialogRef = this._dialog.open(OperationsComponent, {
      data:{accountId:id},
      autoFocus: false,
      width: '650px',
      height: 'auto',
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
