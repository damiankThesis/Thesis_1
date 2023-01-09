import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user/user.service";
import {User} from "../../model/user.model";
import {RentModel} from "../../model/rent.model";
import {BuyModel} from "../../model/buy.model";
import {RentAndBuyService} from "../../service/rentAndBuy/rentAndBuy.service";
import {MatDialog} from "@angular/material/dialog";
import {UserEditComponent} from "../user/user-edit/user-edit.component";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  info: User;
  rents: RentModel[];
  buys: BuyModel[];

  constructor(
    private router: Router,
    private userService: UserService,
    private readonly rentBuyService: RentAndBuyService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.userService.getUser(0).subscribe({
      next: (value) => {
        this.info = value;
        this.getUserInfo();
      },
      error: () => this.router.navigate(["/error"])
    });
  }

  private getUserInfo() {
    this.rentBuyService.getUserRent(this.info.id).subscribe({
      next: (value) => {
        this.rents = value;
      },
      error: () => this.router.navigate(["/error"])
    })

    this.rentBuyService.getUserBuy(this.info.id).subscribe({
      next: (value) => {
        this.buys = value;
      },
      error: () => this.router.navigate(["/error"])
    })
  }

  edit(id: number): void {
    this.dialog.open(UserEditComponent, {
      width: 'auto',
      height: 'auto',
      autoFocus: true,
      data: {
        id: id
      }
    });
  }

}
