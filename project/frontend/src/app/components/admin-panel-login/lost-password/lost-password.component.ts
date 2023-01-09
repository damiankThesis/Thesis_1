import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoginService} from "../service/login.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-lost-password',
  templateUrl: './lost-password.component.html',
  styleUrls: ['./lost-password.component.scss']
})
export class LostPasswordComponent implements OnInit {

  formGroup!: FormGroup;
  formError = "";

  hash = "";
  formGroupChangePassword!: FormGroup;
  formChangePasswordError = "";

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,

  ) { }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      username: ['', Validators.required]
    });

    this.hash = this.route.snapshot.params['hash'];

    this.formGroupChangePassword = this.formBuilder.group({
      password: ['', Validators.required],
      repeatPassword: ['', Validators.required]
    });
  }

  send() {
    if(this.formGroup.valid){
      this.loginService.lostPassword(this.formGroup.value)
        .subscribe({
          next: result => {
            this.formError = "";
            this.formGroup.reset();
            this.snackBar.open('Email with a link has been sent', '', {
              duration: 3000, panelClass: ['success-snackbar'] });
          },
          error: () => {
            this.snackBar.open('The link has expired! Try again.', 'OK',
              { duration: 5000,
                panelClass: ['failed-snackbar']
              });
          }
        });
    }
  }

  sendChangePassword() {
    if (this.formGroupChangePassword.valid && this.passwordIdentical(this.formGroupChangePassword.value)) {
      this.loginService.changePassword({
        password: this.formGroupChangePassword.get("password")?.value,
        repeatPassword: this.formGroupChangePassword.get("repeatPassword")?.value,
        hash: this.hash
      }).subscribe({
        next: () => {
          this.formChangePasswordError = ""
          this.formGroupChangePassword.reset();
          this.snackBar.open('Hasło zostało zmienione', '', {
            duration: 3000, panelClass: ['success-snackbar'] });
        },
        error: () => {
          this.snackBar.open('This car is rented in this range!', 'OK',
            { duration: 5000,
              panelClass: ['failed-snackbar']
            });
        }
      });

    }
  }


  private passwordIdentical(changePassword: any) {
    if (changePassword.password === changePassword.repeatPassword) {
      this.formChangePasswordError = "";
      return true;
    }
    this.formChangePasswordError = "The passwords are different";
    return false;
  }

}
