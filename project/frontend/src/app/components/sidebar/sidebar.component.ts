import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  @Output() sidebarData = new EventEmitter<
    { seatsMin: number,
      priceMin: number,
      priceMax: number,
      powerMin: number,
      powerMax: number,
      capMin: number,
      capMax: number,
    }>();

  seatsMin: number ;
  priceMin: number ;
  priceMax: number ;
  powerMin: number ;
  powerMax: number ;
  capMin: number ;
  capMax: number ;

  constructor() { }

  ngOnInit(): void { }

  apply() {
    this.sidebarData.emit({
      seatsMin: this.seatsMin,
      priceMin: this.priceMin,
      priceMax: this.priceMax,
      powerMin: this.powerMin,
      powerMax: this.powerMax,
      capMin: this.capMin,
      capMax: this.capMax,
    });
  }

}
