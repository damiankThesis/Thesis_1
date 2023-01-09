import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-rent-summary',
  templateUrl: './operation-rent-details.component.html',
  styleUrls: ['./operation-rent-details.component.scss']
})
export class OperationRentDetailsComponent implements OnInit {
  errorMessage: boolean = false

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
  }
}
