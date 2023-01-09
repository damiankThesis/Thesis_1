import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {Car} from "../../model/car.model";
import {ActivatedRoute, Router} from "@angular/router";
import {CarService} from "../../service/car/car.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {RentSummaryComponent} from "./rent-summary/rent-summary.component";
import {RentAndBuyService} from "../../service/rentAndBuy/rentAndBuy.service";
import {JwtService} from "../../service/jwt/jwt.service";

@Component({
  selector: 'app-rent-car',
  templateUrl: './rent-car.component.html',
  styleUrls: ['./rent-car.component.scss']
})
export class RentCarComponent implements OnInit {

  @Input()
  todayDate: Date = new Date();

  @Input()
  endDate: Date = new Date();

  car!: Car;
  showPrice: boolean = false;
  price: number;
  discount: number = 0;
  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  constructor(
    private router: ActivatedRoute,
    private route: Router,
    private carService: CarService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private rentService: RentAndBuyService,
    private readonly jwt: JwtService,
  ) { }

  ngOnInit(): void {
    if(!this.jwt.getToken())
      this.route.navigate(["/login"]).then()
    else {
      this.endDate.setDate(this.todayDate.getDate() + 365);
      this.todayDate.setDate(this.todayDate.getDate() + 1);
      let id = Number(this.router.snapshot.queryParams['carId']);
      this.route.navigate(["/rent"]).then();
      this.getCar(id);
      this.getRent(id);
    }
  }

  public getCar(id: number) {
    this.carService.getCar(id).subscribe({
      next: (car) => {
        this.car = car;
      },
      error: (er: any) => {console.log(er); this.route.navigate(["/cars"]).then();}
    });
  }

  selectRange() {
    this.showPrice= !this.showPrice && this.range.controls.start.value ? !this.showPrice : this.showPrice;

    // @ts-ignore
    this.price = ((this.range.controls.end.value?.getTime() - this.range.controls.start.value?.getTime()) / (1000 * 3600 * 24))*this.car.rentBasePrice;
  }

  rentCar(id: number) {
    if(this.range.controls.start.value && this.range.controls.end.value) {
      this.dialog.open(RentSummaryComponent, {
        width: 'auto',
        height: 'auto',
        autoFocus: true,
        data: {
          car: this.car,
          range: this.range,
          discount: this.discount,
          price: this.price
        }
      });
    }
    else {
      this.snackBar.open('You must select a date range!', 'OK',
        { duration: 6000,
          panelClass: ['failed-snackbar']
        });
    }
  }

  dateBooking: Array<Array<any>> = [];

  getRent(id: number) {
      this.rentService.getCarRentDate(id).subscribe({
        next: (re) => {
          re.forEach((element: any) => {
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
      if (d! >= this.dateBooking[i][0] && d! <= this.dateBooking[i][1])
        return false;
    }
    return true;
  };


}
