import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {RentAndBuyService} from "../../../service/rentAndBuy/rentAndBuy.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {BuyModel} from "../../../model/buy.model";

@Component({
  selector: 'app-buy-summary',
  templateUrl: './buy-summary.component.html',
  styleUrls: ['./buy-summary.component.scss']
})
export class BuySummaryComponent implements OnInit {
  errorMessage: boolean = false;
  description: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private readonly buyService: RentAndBuyService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {}

  buyCar() {
    this.buyService.addBuy({
      car: this.data.car.id,
      meetDate: this.data.date.controls.start.value,
      description: this.description || '',
      totalBuyCost: this.data.car.buyPrice + this.data.discount
    } as BuyModel)
      .subscribe({
        next: (response) => {
          if(response) {
            this.snackBar.open('You are one step before buy this car!', 'OK',
              { duration: 3000,
                panelClass: ['success-snackbar']
              });
          }
          else this.snackBar.open('This car is rented in this range or you already bought it', 'OK',
        { duration: 5000,
                panelClass: ['failed-snackbar']
              });
        },
        error: () => {
          this.snackBar.open('Error!', 'OK',
            { duration: 3000,
              panelClass: ['failed-snackbar']
            });
        }
      });
  }

}
