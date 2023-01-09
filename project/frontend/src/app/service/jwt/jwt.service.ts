import {Injectable} from "@angular/core";
import jwt_decode from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  hasAdminAccess: boolean = false;
  logIn: boolean = false;
  userId: number;

  constructor() {}

  setToken(token: string) {
    localStorage.setItem("token", token);
  }

  getToken(): string | null {
    return localStorage.getItem("token");
  }

  isLoggedIn(): boolean {
    let token = localStorage.getItem("token");
    return token!=null && this.notExpired(token);
  }

  private notExpired(token: string): boolean {
    let decodedJwt = jwt_decode<any>(token);
    return (decodedJwt.exp * 1000) > new Date().getTime()
  }

  public setAdminAccess(adminAccess: boolean) {
    localStorage.setItem("admin", String(adminAccess));
    this.hasAdminAccess = adminAccess;
  }

  public getAdminAccess(): boolean {
    return this.hasAdminAccess || localStorage.getItem("admin")==="true";
  }

  public setLogIn(loggin: boolean) {
    this.logIn = loggin;
  }

  public getLogIn(): boolean {
    return this.logIn;
  }

  public setUserId(id: number) {
    this.userId = id;
  }

  public getUserId(): number {
    return this.userId;
  }

}
