import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../../../model/user.model";
import {UserService} from "../../../service/user/user.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.scss']
})
export class UserEditComponent implements OnInit {

  userEditForm!: FormGroup;
  user: User;

  constructor(
    private form: FormBuilder,
    private readonly userService: UserService,
    private readonly snackBar: MatSnackBar,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    this.userEditForm = this.form.group({
      name: ["", [Validators.required, Validators.minLength(3)]],
      surname: ["", [Validators.required, Validators.minLength(3)]],
      username: ["", [Validators.required, Validators.email]],
      phone: ["", Validators.required],
    });

    let id = this.data.id;
    this.userService.getUser(id).subscribe({
      next: (user) => {
        this.user = user;
        this.userEditForm.setValue({
          name: user.name,
          surname: user.surname,
          username: user.username,
          phone: user.phone
        });
      },
      error: () => console.log("error")
    });
  }


  edit() {
    let id = this.data.id;
    if(this.userEditForm.valid) {
      this.userService.editUser(id, this.userEditForm.value)
        .subscribe({
          next: (response: any) => {
            if(!response) {
              this.snackBar.open('Personal info was successfully edited!', 'OK',
                { duration: 2000,
                  panelClass: ['success-snackbar']
                });

              setTimeout(() => {
                this.router.navigate(["/profile"]).then(()=>window.location.reload());
              }, 1900);
            }
          },
          error: () => {
            this.snackBar.open('Personal info has not been edited!', 'OK',
              { duration: 3000,
                panelClass: ['failed-snackbar']
              });
          }
        });
    }
    this.snackBar.open('Validation failed!', 'OK',
      { duration: 3000,
        panelClass: ['failed-snackbar']
      });
  }

}
