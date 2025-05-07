import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Account} from '../model/account.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http:HttpClient) { }
  public getAccounts():Observable<Array<Account>>{
    return this.http.get<Array<Account>>(environment.backendHost+'/accounts');
  }
  public getAccount(id:string):Observable<Account>{
    return this.http.get<Account>(environment.backendHost+'/accounts/'+id);
  }
  public searchAccounts(keyword:string):Observable<Array<Account>>{
    return this.http.get<Array<Account>>(environment.backendHost+'/accounts/'+keyword);
  }
}
