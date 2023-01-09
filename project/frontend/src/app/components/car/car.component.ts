import {Component, Input, OnInit} from '@angular/core';
import {Car} from "../../model/car.model";
import {CarService} from "../../service/car/car.service";
import {FormControl, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {JwtService} from "../../service/jwt/jwt.service";
import {ConfirmActionDialogService} from "../../service/dialogConfirmAction/confirm-action-dialog.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PageModel} from "../../model/page.model";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {RentModel} from "../../model/rent.model";
import {BuyModel} from "../../model/buy.model";
import {CarHistoryComponent} from "./car-history/car-history.component";

@Component({
  selector: 'app-car',
  templateUrl: './car.component.html',
  styleUrls: ['./car.component.scss'],
})
export class CarComponent implements OnInit {

  @Input()
  todayDate: Date = new Date();

  @Input()
  id: number = 0;

  public cars: Car[]=[];
  page?: PageModel<Car>;
  admin: boolean = false;
  loaded: boolean = true;
  show: boolean = false;
  rent: RentModel[];
  buy: BuyModel[];
  seatsMin: number = 0;
  priceMin: number = 0;
  priceMax: number = 999999;
  powerMin: number = 0;
  powerMax: number = 99999;
  capMin: number = 0;
  capMax: number = 9999;
  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  constructor(
    private carService: CarService,
    private router: Router,
    private readonly jwtService: JwtService,
    private dialogService: ConfirmActionDialogService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.admin = localStorage.getItem("admin")==='true';

    if(this.admin) this.getCarsAdmin(0, 10);
    else this.getCars(0, 10);
  }

  public getCarsAdmin(page: number, size: number): void {
    this.carService.getCarsAdmin(page, size).subscribe({
      next: (value) => {
      this.page = value;
      },
      error: () => this.router.navigate(["/error"])
    });
    this.loaded = false;
  }

  public getCars(page: number, size: number): void {
    this.carService.getCars(page, size).subscribe({
      next: (value) => {
        this.page = value;
      },
      error: () => this.router.navigate(["/error"])
    });
    this.loaded = false;
  }

  blockCar(id: number) {
    this.dialogService.openConfirmDeleteDialog('Are you sure to change car status?')
      .afterClosed().subscribe( (result: any) => {
       if(result) {
          this.carService.toggleStatus(id).subscribe({
            next: () => {
              this.snackBar.open('Car status was changed', 'OK',
                { duration: 3000,
                  panelClass: ['success-snackbar']
                });
              this.router.navigate(["/"]).then();
            },
            error: () => {
              this.snackBar.open('Sth gone wrong!!', 'OK',
                { duration: 3000,
                  panelClass: ['failed-snackbar']
                });
            }
          });
       }
    });
  }

  deleteCar(id: number) {
    this.dialogService.openConfirmDeleteDialog('Are you sure to delete this car? Car was sold?')
      .afterClosed().subscribe( (result: any) => {
        if(result) {
          this.carService.deleteCar(id).subscribe({
            next: () => {
              this.snackBar.open('Car was deleted', 'OK',
                { duration: 3000,
                  panelClass: ['success-snackbar']
                });
              this.router.navigate(["/"]).then();
            },
            error: (er: any) => {
              if(er.status===500)
                this.snackBar.open('Cannot delete because this car is rented!!', 'OK',
                  { duration: 3000,
                    panelClass: ['failed-snackbar']
                  });
              else this.snackBar.open('Sth gone wrong!!', 'OK',
                  { duration: 3000,
                    panelClass: ['failed-snackbar']
                });
            }
          });
        }
    });
  }

  onPageEvent(event: PageEvent) {
    if(this.admin) this.getCarsAdmin(event.pageIndex, event.pageSize)
    else this.getCars(event.pageIndex, event.pageSize)
  }

  showOperations(id: number) {
    this.dialog.open(CarHistoryComponent, {
      width: 'auto',
      height: 'auto',
      autoFocus: true,
      data: {
        id: id,
      }
    });
  }

  onBookAdded(eventData: { seatsMin: number, priceMin: number, priceMax: number, powerMin: number, powerMax: number
              ,capMin: number, capMax: number}) {
      this.seatsMin = eventData.seatsMin || 0;
      this.priceMin = eventData.priceMin || 0;
      this.priceMax = eventData.priceMax || 999999;
      this.powerMin = eventData.powerMin || 0;
      this.powerMax = eventData.powerMax || 999999;
      this.capMin = eventData.capMin || 0;
      this.capMax = eventData.capMax || 9999;
  };

  check(car: Car) {
    return this.seatsMin<=car.carDetail.seats
      && this.priceMin<=car.rentBasePrice
      && this.priceMax>=car.rentBasePrice
      && (this.powerMin<=car.carDetail.power && this.powerMax>=car.carDetail.power)
      && (this.capMin<=car.carDetail.capacity && this.capMax>=car.carDetail.capacity)
  }
}
