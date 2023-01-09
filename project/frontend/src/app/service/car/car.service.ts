import { Observable } from "rxjs";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Car} from "../../model/car.model";
import {environment} from "../../../environments/environment";
import {PageModel} from "../../model/page.model";
import {UpdateWithImage} from "../../model/updateWithImage";
import {CarDTO} from "../../model/carCreateDTO.model";

@Injectable({providedIn: 'root'})
export class CarService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getCarsAdmin(page: number, size: number): Observable<PageModel<Car>> {
    return this.http.get<PageModel<Car>>(`${this.apiServerUrl}/api/v1/cars?page=${page}&size=${size}`);
  }

  public getCars(page: number, size: number): Observable<PageModel<Car>> {
    return this.http.get<PageModel<Car>>(`${this.apiServerUrl}/api/v1/cars/available?page=${page}&size=${size}`);
  }

  public addCar(carInfo: CarDTO): Observable<CarDTO> {
    return this.http.post<CarDTO>(`${this.apiServerUrl}/api/v1/cars`, carInfo);
  }

  public editCar(carId: number, carEdit: Car): Observable<CarDTO> {
    return this.http.put<CarDTO>(`${this.apiServerUrl}/api/v1/cars/${carId}`, carEdit);
  }

  public getCar(id: number): Observable<Car> {
    return this.http.get<Car>(`${this.apiServerUrl}/api/v1/cars/${id}`);
  }

  public getEditCar(id: number): Observable<UpdateWithImage> {
    return this.http.get<UpdateWithImage>(`${this.apiServerUrl}/api/v1/cars/${id}`);
  }

  public toggleStatus(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiServerUrl}/api/v1/cars/${id}`, null);
  }

  public deleteCar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/api/v1/cars/${id}`);
  }
}
