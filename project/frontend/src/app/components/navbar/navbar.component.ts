import { Component, OnInit } from '@angular/core';
import {  Router } from '@angular/router';
import {  NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup } from "@angular/forms";
import { JwtService } from "../../service/jwt/jwt.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  auth: boolean;
  admin: boolean;
  formGroup!: FormGroup;

  constructor(
    private readonly jwtService: JwtService,
    private modalService: NgbModal,
    private router: Router
  ) { }

  ngOnInit(): void {

    this.auth = !this.jwtService.getToken() ? false : true;
    this.admin = localStorage.getItem("admin")==='true';
  }

  open(content: any) {
    this.modalService.open(content);
  }

  logout() {
    this.auth = false;
    this.admin = false;
    localStorage.removeItem("token");
    localStorage.clear();
    this.router.navigate(["/cars"]).then(() => window.location.reload());
  }

}
