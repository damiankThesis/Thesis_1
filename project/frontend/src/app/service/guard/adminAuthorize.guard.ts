import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {JwtService} from "../jwt/jwt.service";
import {Injectable} from "@angular/core";

@Injectable()
export class AdminAuthorizeGuard implements CanActivate {

  constructor(
    private readonly jwtService: JwtService,
    private readonly router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if( !this.jwtService.getToken() || !this.jwtService.getAdminAccess())
      this.router.navigate(["/login"]);
    return true;
  }
}
