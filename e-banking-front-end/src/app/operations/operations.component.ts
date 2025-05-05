import {Component, Inject, OnInit} from '@angular/core';
import {OperationService} from '../services/operation.service';
import {AccountOperation, Operation} from '../model/operation.model';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {LoadingService} from '../services/loading.service';
import {Observable} from 'rxjs';
import {PageEvent} from '@angular/material/paginator';

@Component({
  selector: 'app-operations',
  standalone: false,
  templateUrl: './operations.component.html',
  styleUrl: './operations.component.css'
})
export class OperationsComponent implements OnInit {
  operations!: Operation;
  accountOperations!: Array<AccountOperation>;
  size: number = 2;
  page: number = 0;
  accountId!: string;
  isLoading$!: Observable<boolean>;
  displayedColumns: string[] = ['id', 'operationDate', 'amount', 'type', 'description'];
  constructor(private operationService:OperationService,@Inject(MAT_DIALOG_DATA) public data: any,private loadingService: LoadingService) {
    this.accountId = data.accountId;
  }

  ngOnInit(): void {
    this.isLoading$ = this.loadingService.isLoading$;
    this.loadOperations();
  }
  loadOperations() {
    this.loadingService.show();
    this.operationService.getOperations(this.accountId, this.page, this.size).subscribe({
      next: (data) => {
        this.operations = data;
        this.accountOperations= this.operations.accountOperations;
        console.log(this.accountOperations);
        console.log("size :"+this.size + " page : "+this.page);
        this.loadingService.hide();
      },
      error: (err) => {
        console.log(err);
        this.loadingService.hide();
      }
    });
  }
  onPageChange(event: PageEvent) {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.loadOperations();
  }

}
