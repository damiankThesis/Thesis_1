import { Component, OnInit, ViewChild } from '@angular/core';
import {UserService} from "../../service/user/user.service";
import {Router} from "@angular/router";
import {ConfirmActionDialogService} from "../../service/dialogConfirmAction/confirm-action-dialog.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatSort, Sort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {UserEditComponent} from "./user-edit/user-edit.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  users: any;
  displayedColumns: string[] = ["name",  "username", "phone", "numberOfRent", "numberOfBuy", "delete"];
  @ViewChild(MatSort) sort: MatSort;

  dataSource: MatTableDataSource<any>;

  constructor(
    private userService: UserService,
    private router: Router,
    private dialogService: ConfirmActionDialogService,
    private snackBar: MatSnackBar,
    private _liveAnnouncer: LiveAnnouncer
  ) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.userService.getAll().subscribe({
      next: (response) => {
        this.users = response.body;
        this.users = this.users.slice(1,);
        this.dataSource = new MatTableDataSource(this.users);
        this.dataSource.sort = this.sort;
      },
      error: () => this.router.navigate(["/error"])
    });
  }

  deleteUser(id: number) {
    this.dialogService.openConfirmDeleteDialog('Are you really sure to delete this user?')
      .afterClosed().subscribe( (result: any) => {
      if(result) {
        this.userService.deleteUser(id).subscribe({
          next: () => {
            this.snackBar.open('User was deleted', 'OK',
              { duration: 3000,
                panelClass: ['success-snackbar']
              });
            this.router.navigate(["/admin-dashboard/users-management"]).then(()=>window.location.reload());
          },
          error: () => {
              this.snackBar.open('Cannot delete because user is renting a car!!', 'OK',
                { duration: 3000,
                  panelClass: ['failed-snackbar']
                });
          }
        });
      }
    });
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    else this._liveAnnouncer.announce('Sorting cleared');
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
