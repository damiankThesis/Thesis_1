import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {UploadModel} from "../../model/upload.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  uploadImage(formData: FormData): Observable<UploadModel> {
    return this.http.post<UploadModel>(`${this.apiServerUrl}/api/v1/cars/upload`, formData);
  }
}
