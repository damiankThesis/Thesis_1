import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "./service/login.service";
import {JwtService} from "../../service/jwt/jwt.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-panel-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})

export class LoginComponent implements OnInit {

  loginFormGroup!: FormGroup;
  loginError: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private adminLoginService: LoginService,
    private jwtService: JwtService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.loginFormGroup = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submit() {
    if(this.loginFormGroup.valid) {
      this.adminLoginService.login(this.loginFormGroup.value)
        .subscribe({
          next: (response) => {
            this.loginError = false;
            if(response.hasAdminAccess) {
              this.jwtService.setAdminAccess(true);
              this.router.navigate(["/admin-dashboard"]).then(() => window.location.reload());
            }
            else {
              this.router.navigate(["/cars"]).then(() => window.location.reload());
            }
            this.jwtService.setToken(response.token);
          },
          error: () => this.loginError = true
        })
    }
  }

}
