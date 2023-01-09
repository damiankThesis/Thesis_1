import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from "../../model/user.model";
import { ListResponseModel} from "../../model/response/listResponseModel";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getAll(): Observable<ListResponseModel<User>> {
    return this.http.get<ListResponseModel<User>>(`${this.apiServerUrl}/api/v1/users`);
  }

  public deleteUser(id: number): any {
    return this.http.delete(`${this.apiServerUrl}/api/v1/users/${id}`);
  }

  public getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiServerUrl}/api/v1/users/${id}`);
  }

  public editUser(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.apiServerUrl}/api/v1/users/${id}`, user);
  }

}
