import {Component, Input, OnInit} from '@angular/core';
import {Car} from "../../model/car.model";
import {ActivatedRoute, Router} from "@angular/router";
import {CarService} from "../../service/car/car.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {BuySummaryComponent} from "./buy-summary/buy-summary.component";
import {RentAndBuyService} from "../../service/rentAndBuy/rentAndBuy.service";
import {JwtService} from "../../service/jwt/jwt.service";

@Component({
  selector: 'app-buy-car',
  templateUrl: './buy-car.component.html',
  styleUrls: ['./buy-car.component.scss']
})
export class BuyCarComponent implements OnInit {

  @Input()
  todayDate: Date = new Date();

  @Input()
  endDate: Date = new Date();

  car!: Car;
  discount: number = 0;
  date = new FormGroup({
    start: new FormControl<Date | null>(null),
  });
  dateBooking: Array<Array<any>> = [];

  constructor(
    private router: ActivatedRoute,
    private route: Router,
    private carService: CarService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private readonly rentService: RentAndBuyService,
    private readonly jwt: JwtService,
  ) { }

  ngOnInit(): void {
    if(!this.jwt.getToken())
      this.route.navigate(["/login"]).then()
    else {
      this.endDate.setDate(this.todayDate.getDate() + 30);
      let id = Number(this.router.snapshot.queryParams['carId']);
      this.route.navigate(["/buy"]).then();
      this.getCar(id);
      this.getRent(id);
    }
  }

  public getCar(id: number) {
    this.carService.getCar(id).subscribe({
      next: (car) => {
        this.car = car;
      },
      error: () => console.log("error")
    });
  }

  buyCar() {
    if(this.date.controls.start.value) {
      this.dialog.open(BuySummaryComponent, {
        width: 'auto',
        height: 'auto',
        autoFocus: true,
        data: {
          car: this.car,
          date: this.date,
          discount: this.discount
        }
      });
    }
    else {
      this.snackBar.open('You must select a date!', 'OK',
        { duration: 6000,
          panelClass: ['failed-snackbar']
        });
    }
  }

  getRent(id: number) {
    this.rentService.getCarRentDate(id).subscribe({
      next: (date) => {
        date.forEach((element: any) => {
          this.dateBooking.push([
            new Date(element[0]),
            new Date(element[1]),
          ]);
        });
      },
      error: () => console.log("error")
    });
  }

  myFilter = (d: Date | null ): any => {
    for (let i = 0; i < this.dateBooking.length; i++) {
      if (d! <= this.dateBooking[i][1])
        return false;
    }
    return true;
  };

}
