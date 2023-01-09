import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {PanelRegisterService} from "./service/panel-register.service";
import {JwtService} from "../../service/jwt/jwt.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerFormGroup!: FormGroup;
  formError: boolean = false;
  doConfirm: boolean = false;
  admin: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private registerService: PanelRegisterService,
    private router: Router,
    private readonly jwtService: JwtService,
    private readonly snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    this.registerFormGroup = this.formBuilder.group({
      name: ["", [Validators.required, Validators.minLength(3)]],
      surname: ["", [Validators.required, Validators.minLength(3)]],
      username: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, Validators.minLength(8)]],
      repeatPassword: ["", [Validators.required, Validators.minLength(8)]],
      phone: ["", Validators.required],
    });
  }

  submit() {
    if(this.registerFormGroup.valid) {
      this.registerService.register(this.registerFormGroup.value)
        .subscribe({
          next: (response) => {
            this.formError = false;
            this.doConfirm = false;
            this.jwtService.setToken(response.token);

            this.snackBar.open('Car was successfully added!', 'OK',
              { duration: 3000,
                panelClass: ['success-snackbar']
              });
          },
          error: (error) => {
            if(error.status==403) {
              this.snackBar.open('Please go to your email and confirm account!', 'OK',
                {
                  duration: 6000,
                  panelClass: ['success-snackbar']
                });
              this.registerFormGroup.reset();
              this.router.navigate(["/login"]);
            }
            else if(error.error.message==='istnieje'){
              this.snackBar.open('User with this email already exist!', 'OK',
                { duration: 5000,
                  panelClass: ['failed-snackbar']
                });
            }
            else this.snackBar.open('The passwords are not the same!', 'OK',
              { duration: 5000,
                panelClass: ['failed-snackbar']
              });
          }
        });
    }
  }

}
