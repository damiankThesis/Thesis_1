import {Component, OnInit, ViewChild} from '@angular/core';
import {RentAndBuyService} from "../../../service/rentAndBuy/rentAndBuy.service";
import {Router} from "@angular/router";
import {RentModel} from "../../../model/rent.model";
import {MatSort, Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {ConfirmActionDialogService} from "../../../service/dialogConfirmAction/confirm-action-dialog.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {OperationRentDetailsComponent} from "./operation-details/operation-rent-details.component";
import {OperationEditComponent} from "../operation-edit/operation-edit.component";


@Component({
  selector: 'app-operations-rent',
  templateUrl: './operations-rent.component.html',
  styleUrls: ['./operations-rent.component.scss']
})
export class OperationsRentComponent implements OnInit {

  rentsAndBuy: RentModel[];
  rent: RentModel;
  displayedColumns: string[] = ["name", "username", "phone", "startDate", "endDate", "car", "totalCost", "payment", "delete", "details"];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<any>;

  constructor(
    private rentService: RentAndBuyService,
    private router: Router,
    private _liveAnnouncer: LiveAnnouncer,
    private dialogService: ConfirmActionDialogService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.getAllOperations();
  }

  getAllOperations(): void {
    this.rentService.getAllRents().subscribe({
      next: (response) => {
        this.rentsAndBuy = response;
        this.dataSource = new MatTableDataSource(this.rentsAndBuy);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: () => this.router.navigate(["/error"])
    });
  }

  compareDate(endDate: Date): boolean {
    return new Date(endDate).getTime() <= Date.now();
  }

  isNotStartedYet(startDate: Date): boolean {
    return new Date(startDate).getTime() > Date.now() ;
  }

  showDetails(id: number) {
    this.rentService.getRent(id).subscribe({
      next: (re) => {
        this.rent = re;
        this.showUserAndCarDetails();
      },
      error: () => console.log("error")
    });
  }


  showUserAndCarDetails(): void {
    this.dialog.open(OperationRentDetailsComponent, {
      width: 'auto',
      height: 'auto',
      autoFocus: true,
      data: {
        car: this.rent?.car,
        user: this.rent?.user,
        description: this.rent?.description,
        rentDate: this.rent?.rentDate
      }
    });
  }

  delete(id: number) {
    this.dialogService.openConfirmDeleteDialog('Are you sure to delete?')
      .afterClosed().subscribe( (result: any) => {
      if(result) {
        this.rentService.deleteRent(id).subscribe({
          next: () => {
            this.snackBar.open('Rent was deleted', 'OK',
              { duration: 3000,
                panelClass: ['success-snackbar']
              });
            this.router.navigate(["/admin-dashboard/operations-car"]).then( () => window.location.reload());
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

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
