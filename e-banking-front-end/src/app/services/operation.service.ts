import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Operation} from '../model/operation.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OperationService {

  constructor(private httpClient:HttpClient) { }
  public getOperations(accountId:string,page:number,size:number):Observable<Operation>{
    return this.httpClient.get<Operation>(environment.backendHost+`/accounts/${accountId}/pageOperations?size=${size}&page=${page}`);
  }
}
