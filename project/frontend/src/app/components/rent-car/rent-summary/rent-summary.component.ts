import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {RentAndBuyService} from "../../../service/rentAndBuy/rentAndBuy.service";
import {RentModel} from "../../../model/rent.model";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";

@Component({
  selector: 'app-rent-summary',
  templateUrl: './rent-summary.component.html',
  styleUrls: ['./rent-summary.component.scss']
})
export class RentSummaryComponent implements OnInit {
  errorMessage: boolean = false
  rent: RentModel;
  description: string;

  paymentMethod: string ='PRZELEWY24_ONLINE';
  ship: string = 'self';

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private readonly rentService: RentAndBuyService,
    private readonly router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void { }

  rentCar() {
    this.rentService.addRent({
      car: this.data.car.id,
      startDate: this.data.range.controls.start.value,
      endDate: this.data.range.controls.end.value,
      description: this.description || '',
      totalCost: this.data.price + this.data.discount,
      payment: this.paymentMethod
    } as RentModel)
      .subscribe({
      next: (response) => {
        if(response) {
          this.snackBar.open('Car was successfully rented!', 'OK',
            { duration: 3000,
              panelClass: ['success-snackbar']
            });

          if(response.redirectUrl)
            window.location.href = response.redirectUrl;
        }
        else {
          this.snackBar.open('This car is rented in this range!', 'OK',
            { duration: 5000,
              panelClass: ['failed-snackbar']
            });
        }
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
