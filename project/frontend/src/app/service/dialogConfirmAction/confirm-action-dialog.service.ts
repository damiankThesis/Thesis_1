import { Injectable } from '@angular/core';
import {ConfirmDialogComponent} from "../../components/confirm-dialog/confirm-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Injectable({
  providedIn: 'root'
})
export class ConfirmActionDialogService {

  constructor(private dialog: MatDialog) { }

  openConfirmDeleteDialog(message: string): any {
    return this.dialog.open(ConfirmDialogComponent, {
      width: '360px',
      panelClass: 'confirm-dialog-container',
      disableClose: true,
      position: {top: "200px"},
      data: {
        message: message
      }
    });
  }
}
