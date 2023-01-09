import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PanelRegisterService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  register(credential: any): Observable<any> {
    return this.http.post(`${this.apiServerUrl}/api/v1/register`, credential);
  }

  confirmAccount(credential: any): Observable<any> {
    return this.http.post(`${this.apiServerUrl}/api/v1/verified`, credential);
  }
}
