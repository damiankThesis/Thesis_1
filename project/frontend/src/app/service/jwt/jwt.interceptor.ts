import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {JwtService} from "./jwt.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private readonly jwt: JwtService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = this.jwt.getToken();

    if(token &&
      (req.url.startsWith("http://localhost:8080/api/v1/cars"))
        || req.url.startsWith("http://localhost:8080/api/v1/rent")
        || req.url.startsWith("http://localhost:8080/api/v1/users")
        || req.url.startsWith("http://localhost:8080/api/v1/buy")) {
      req = req.clone({
          headers: req.headers.set("Authorization", "Bearer " + token)
        });
    }
    return next.handle(req);

  }
}
