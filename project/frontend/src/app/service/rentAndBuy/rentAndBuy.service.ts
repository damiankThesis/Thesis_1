import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {RentModel} from "../../model/rent.model";
import {BuyModel} from "../../model/buy.model";

@Injectable({
  providedIn: 'root'
})
export class RentAndBuyService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getAllRents(): Observable<RentModel[]> {
    return this.http.get<RentModel[]>(`${this.apiServerUrl}/api/v1/rent`);
  }

  public addRent(rentInfo: any): Observable<any> {
    return this.http.post(`${this.apiServerUrl}/api/v1/rent`, rentInfo);
  }

  public getAllBuy(): Observable<BuyModel[]> {
    return this.http.get<BuyModel[]>(`${this.apiServerUrl}/api/v1/buy`);
  }

  public getCarRentDate(id: number): Observable<any> {
    return this.http.get(`${this.apiServerUrl}/api/v1/rent/${id}/date`);
  }

  public getUserRent(id: number): Observable<RentModel[]> {
    return this.http.get<RentModel[]>(`${this.apiServerUrl}/api/v1/rent/user/${id}`);
  }

  public getUserBuy(id: number): Observable<BuyModel[]> {
    return this.http.get<BuyModel[]>(`${this.apiServerUrl}/api/v1/buy/user/${id}`);
  }

  public getCarRent(id: number): Observable<RentModel[]> {
    return this.http.get<RentModel[]>(`${this.apiServerUrl}/api/v1/rent/car/${id}`);
  }

  public getCarBuy(id: number): Observable<BuyModel[]> {
    return this.http.get<BuyModel[]>(`${this.apiServerUrl}/api/v1/buy/car/${id}`);
  }

  public getRent(id: number): Observable<RentModel> {
    return this.http.get<RentModel>(`${this.apiServerUrl}/api/v1/rent/${id}`);
  }

  public getBuy(id: number): Observable<BuyModel> {
    return this.http.get<BuyModel>(`${this.apiServerUrl}/api/v1/buy/${id}`);
  }

  public addBuy(buyInfo: any): Observable<any> {
    return this.http.post(`${this.apiServerUrl}/api/v1/buy`, buyInfo);
  }

  public deleteRent(id: number): any {
    return this.http.delete(`${this.apiServerUrl}/api/v1/rent/${id}`);
  }

  public deleteBuy(id: number): any {
    return this.http.delete(`${this.apiServerUrl}/api/v1/buy/${id}`);
  }

  public getRentStatistics(): Observable<any> {
    return this.http.get(`${this.apiServerUrl}/api/v1/stats/rent`);
  }

  public getBuyStatistics(): Observable<any> {
    return this.http.get(`${this.apiServerUrl}/api/v1/stats/buy`);
  }

}
