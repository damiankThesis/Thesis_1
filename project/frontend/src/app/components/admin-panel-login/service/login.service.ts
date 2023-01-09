import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiServerUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) { }

  login(credential: any): Observable<any> {
    return this.http.post(`${this.apiServerUrl}/api/v1/login`, credential);
  }

  lostPassword(emailObject: any): Observable<any>  {
    return this.http.post(`${this.apiServerUrl}/api/v1/lostPassword`, emailObject);
  }

  changePassword(passwordObject: any): Observable<any> {
    return this.http.post(`${this.apiServerUrl}/api/v1/changePassword`, passwordObject);
  }
}
