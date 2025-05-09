import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatsService {

  constructor(private http: HttpClient) { }
  getDashboardStats(startDate: string, endDate: string): Observable<any> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get(`${environment.backendHost}/stats/operations`, { params });
  }
  getOperationsChartData(startDate: string, endDate: string): Observable<any> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get(`${environment.backendHost}/stats/operations-chart`, { params });
  }
  getAccountsByType(): Observable<{ [type: string]: number }> {
    return this.http.get<{ [type: string]: number }>(`${environment.backendHost}/stats/accounts-by-type`);
  }
}
