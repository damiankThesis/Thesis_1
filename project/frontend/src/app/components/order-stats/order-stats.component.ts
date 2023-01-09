import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {Chart, ChartData, registerables} from 'chart.js';
import {RentAndBuyService} from "../../service/rentAndBuy/rentAndBuy.service";

@Component({
  selector: 'app-order-stats',
  templateUrl: './order-stats.component.html',
  styleUrls: ['./order-stats.component.scss']
})
export class OrderStatsComponent implements AfterViewInit {

  @ViewChild("statsRent") private statsRent!: ElementRef;
  @ViewChild("statsBuy") private statsBuy!: ElementRef;
  chartRent!: Chart;
  chartBuy!: Chart;

  amountSumRent: number = 0;
  valueSumRent: number = 0;
  amountSumBuy: number = 0;
  valueSumBuy: number = 0;

  private dataRent = {
    labels: [],
    datasets: [
      {
        label: 'Rents',
        data: [],
        borderColor: '#3f5fff',
        backgroundColor: '#abd248',
        order: 1,
        yAxisID: 'y'
      },
      {
        label: 'Sum',
        data: [],
        borderColor: '#3f5fff',
        backgroundColor: '#00A1FF ',
        type: 'line',
        order: 0,
        yAxisID: 'y1'
      }
    ]
  } as ChartData;

  private dataBuy = {
    labels: [],
    datasets: [
      {
        label: 'Buy',
        data: [],
        borderColor: '#3f5fff',
        backgroundColor: '#abd248',
        order: 1,
        yAxisID: 'y'
      },
      {
        label: 'Sum',
        data: [],
        borderColor: '#3f5fff',
        backgroundColor: '#00A1FF ',
        type: 'line',
        order: 0,
        yAxisID: 'y1'
      }
    ]
  } as ChartData;

  constructor(private readonly rentBuyService: RentAndBuyService) {
    Chart.register(...registerables);
  }

  ngAfterViewInit(): void {
    this.setUp();
    this.getStats();
  }

  private setUp() {
    this.chartRent = new Chart(this.statsRent.nativeElement, {
      type: 'bar',
      data: this.dataRent,
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Rents'
          }
        },
        scales: {
          y: {
            type: 'linear',
            position: 'left',
            display: true,
            ticks: {
              stepSize: 1
            }
          },
          y1: {
            type: 'linear',
            position: 'right',
            display: true,
            grid: {
              drawOnChartArea: false
            }
          }
        }
      }
    });

    this.chartBuy = new Chart(this.statsBuy.nativeElement, {
      type: 'bar',
      data: this.dataBuy,
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Buy'
          }
        },
        scales: {
          y: {
            type: 'linear',
            position: 'left',
            display: true,
            ticks: {
              stepSize: 1
            }
          },
          y1: {
            type: 'linear',
            position: 'right',
            display: true,
            grid: {
              drawOnChartArea: false
            }
          }
        }
      }
    });
  }

  private getStats() {
    this.rentBuyService.getRentStatistics()
      .subscribe(stats => {
        this.dataRent.labels = stats.labels;
        this.dataRent.datasets[0].data = stats.amount;
        this.dataRent.datasets[1].data = stats.value;
        this.chartRent.update();
        this.amountSumRent = stats.amount.reduce((acc: number, val: number) => acc + val);
        this.valueSumRent = stats.value.reduce((acc: number, val: number) => acc + val);
      });

    this.rentBuyService.getBuyStatistics()
      .subscribe(stats => {
        this.dataBuy.labels = stats.labels;
        this.dataBuy.datasets[0].data = stats.amount;
        this.dataBuy.datasets[1].data = stats.value;
        this.chartBuy.update();
        this.amountSumBuy = stats.amount.reduce((acc: number, val: number) => acc + val);
        this.valueSumBuy = stats.value.reduce((acc: number, val: number) => acc + val);
      });
  }

}
