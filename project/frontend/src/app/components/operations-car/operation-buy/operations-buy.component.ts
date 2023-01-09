import {Component, OnInit, ViewChild} from '@angular/core';
import {RentAndBuyService} from "../../../service/rentAndBuy/rentAndBuy.service";
import {Router} from "@angular/router";
import {MatSort, Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {ConfirmActionDialogService} from "../../../service/dialogConfirmAction/confirm-action-dialog.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {OperationBuyDetailsComponent} from "./operation-details/operation-buy-details.component";
import {BuyModel} from "../../../model/buy.model";

@Component({
  selector: 'app-operations-buy',
  templateUrl: './operations-buy.component.html',
  styleUrls: ['./operations-buy.component.scss']
})
export class OperationsBuyComponent implements OnInit {

  buyAll: BuyModel[];
  buy: BuyModel;
  displayedColumns: string[] = ["name", "username", "phone", "car", "meetDate", "reqDate", "totalBuyCost", "delete", "details"];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<any>;

  constructor(
    private rentService: RentAndBuyService,
    private router: Router,
    private _liveAnnouncer: LiveAnnouncer,
    private dialogService: ConfirmActionDialogService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.getAllOperations();
  }

  getAllOperations(): void {
    this.rentService.getAllBuy().subscribe({
      next: (response) => {
        this.buyAll = response;
        this.dataSource = new MatTableDataSource(this.buyAll);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: () => this.router.navigate(["/error"])
    });
  }

  showDetails(id: number) {
    this.rentService.getBuy(id).subscribe({
      next: (res) => {
        this.buy = res;
        this.showUserAndBuyCarDetails();
      },
      error: () => console.log("error")
    });
  }

  showUserAndBuyCarDetails(): void {
    this.dialog.open(OperationBuyDetailsComponent, {
      width: 'auto',
      height: 'auto',
      autoFocus: true,
      data: {
        car: this.buy?.car,
        user: this.buy?.user,
        description: this.buy?.description,
      }
    });
  }

  delete(id: number) {
    this.dialogService.openConfirmDeleteDialog('Are you sure to delete? Has the client opted out?')
      .afterClosed().subscribe( (result: any) => {
      if(result) {
        this.rentService.deleteBuy(id).subscribe({
          next: () => {
            this.snackBar.open('Buy was deleted', 'OK',
              { duration: 3000,
                panelClass: ['success-snackbar']
              });
            this.router.navigate(["/admin-dashboard/operations-car"]).then(()=>window.location.reload());
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
