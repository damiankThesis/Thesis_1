import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PanelRegisterService} from "../service/panel-register.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['./confirm-account.component.scss']
})
export class ConfirmAccountComponent implements OnInit {

  hash = "";

  constructor(
    private route: ActivatedRoute,
    private registerService: PanelRegisterService,
    private readonly snackBar: MatSnackBar,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.hash = this.route.snapshot.params['hash'];
    this.send();
    this.router.navigate(['/login']).then();
  }

  send() {
    if(this.hash) {
      this.registerService.confirmAccount({
        hash: this.hash
      }).subscribe({
        next: () => {
          this.snackBar.open('The account has been activated! You can log in!', '', {
            duration: 6000, panelClass: ['success-snackbar'] });
        },
        error: () => {
          this.snackBar.open('Activation problem! Please contact your administrator', 'OK',
            { duration: 5000,
              panelClass: ['failed-snackbar']
            });
        }
      });
    }
  }

}
