import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {OperationService} from '../services/operation.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {AccountOperation} from '../model/operation.model';
import {Account} from '../model/account.model';
import {AccountService} from '../services/account.service';

@Component({
  selector: 'app-new-operation',
  standalone: false,
  templateUrl: './new-operation.component.html',
  styleUrl: './new-operation.component.css'
})
export class NewOperationComponent implements OnInit {
  newOperationFormGroup!: FormGroup;
  isEditMode: boolean = false;
  accounts: Account[] = [];
  constructor(private fb:FormBuilder,private operationService:OperationService,private accountService:AccountService,private _snackBar: MatSnackBar,@Inject(MAT_DIALOG_DATA) public data: AccountOperation) { }

  ngOnInit(): void {
    this.newOperationFormGroup= this.fb.group({
      id: [this.data?.id || null],
      bankAccountDTO: [this.data?.bankAccountDTO || null],
      operationDate: [this.data?.operationDate || null],
      amount: [this.data?.amount || null],
      type: [this.data?.type || ''],
      description: [this.data?.description || '']
    });
    this.loadAccounts();
  }

  loadAccounts() {
    this.accountService.getAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
      },
      error: (err) => {
        this._snackBar.open("Error loading accounts", "Close", {duration: 2000});
      }
    });
  }

  handleSaveOperation() {
    const operation=this.newOperationFormGroup.value;
    console.log("Operation to save: ", operation);
    this.operationService.saveOperation(operation).subscribe({
      next: (data) => {
        this._snackBar.open("Operation saved successfully", "Close", {duration: 2000});
        this.newOperationFormGroup.reset();
      },
      error: (err) => {
        this._snackBar.open("Error saving operation", "Close", {duration: 2000});
      }
    });
  }
}
