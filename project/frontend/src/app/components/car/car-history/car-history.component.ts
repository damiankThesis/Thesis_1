import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {RentModel} from "../../../model/rent.model";
import {BuyModel} from "../../../model/buy.model";
import {RentAndBuyService} from "../../../service/rentAndBuy/rentAndBuy.service";

@Component({
  selector: 'app-car-history',
  templateUrl: './car-history.component.html',
  styleUrls: ['./car-history.component.scss']
})
export class CarHistoryComponent implements OnInit {

  rent: RentModel[]=[];
  buy: BuyModel[]=[];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private readonly rentBuyService: RentAndBuyService,
  ) { }

  ngOnInit(): void {
    this.rentBuyService.getCarRent(this.data?.id).subscribe({
      next: (rents) => {
        this.rent = rents;
      },
      error: () => console.log("error")
    });

    this.rentBuyService.getCarBuy(this.data?.id).subscribe({
      next: (b) => {
        this.buy = b;
      },
      error: () => console.log("error")
    });
  }

}
