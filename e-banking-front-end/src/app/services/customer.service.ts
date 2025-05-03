import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {Customer} from '../model/customer.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private customersUpdatedSubject = new BehaviorSubject<boolean>(false);
  public customersUpdated$ = this.customersUpdatedSubject.asObservable();

  constructor(private http:HttpClient) {

  }
  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environment.backendHost+'/customers');
  }
  public searchCustomers(keyword:string):Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environment.backendHost+'/customers/search?keyword='+keyword);
  }
  public saveCustomer(customer:Customer):Observable<Customer>{
    return this.http.post<Customer>(environment.backendHost+'/customers',customer).pipe(
      tap(() => this.notifyCustomersChanged())
    );
  }
  public deleteCustomer(id:number):Observable<void>{
    return this.http.delete<void>(environment.backendHost+'/customers/'+id).pipe(
      tap(() => this.notifyCustomersChanged())
    );
  }
  public getCustomer(id:number):Observable<Customer>{
    return this.http.get<Customer>(environment.backendHost+'/customers/'+id);
  }
  private notifyCustomersChanged() {
    this.customersUpdatedSubject.next(true);
  }
}
