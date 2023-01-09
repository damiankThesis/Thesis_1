import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-buy-summary',
  templateUrl: './operation-buy-details.component.html',
  styleUrls: ['./operation-buy-details.component.scss']
})
export class OperationBuyDetailsComponent implements OnInit {
  errorMessage: boolean = false

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void { }
}
