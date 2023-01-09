import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CarService } from "../../service/car/car.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";

@Component({
  selector: 'app-add-car',
  templateUrl: './add-car.component.html',
  styleUrls: ['./add-car.component.scss']
})
export class AddCarComponent implements OnInit {

  carAddFrom: FormGroup;
  fuel: string;

  constructor(
    private formBuilder: FormBuilder,
    private carService: CarService,
    private snackBar: MatSnackBar,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.createCarAddForm();
  }

  createCarAddForm() {
    this.carAddFrom = this.formBuilder.group({
      brand: ["", [Validators.required, Validators.minLength(3)]],
      model: ["", [Validators.required, Validators.minLength(3)]],
      registration: ["", [Validators.required, Validators.minLength(4)]],
      buyPrice: ["", [Validators.required]],
      rentBasePrice: ["", [Validators.required]],
      availableStatus: ["", [Validators.required]],
      type: [""],
      productionYear: [""],
      fuel: [""],
      power: [""],
      capacity: [""],
      gearbox: [""],
      drive: [""],
      doors: [""],
      seats: [""],
      color: [""],
      description: [""],
    });
  }

  addCar() {
    if(this.carAddFrom.valid) {
      this.carService.addCar(this.carAddFrom.value)
        .subscribe({
          next: (response) => {
            if(response) {
              this.snackBar.open('Car was successfully added!', 'OK',
                { duration: 3000,
                  panelClass: ['success-snackbar']
                });
              setTimeout(() => {
                this.router.navigate(['/admin-dashboard/edit-car', response.id]);
              }, 1500);  //5s
            }
          },
          error: () => {
            this.snackBar.open('Car has not been added!', 'OK',
              { duration: 3000,
                panelClass: ['failed-snackbar']
              });
          }
        });
    }

    this.snackBar.open('Validation failed!', 'OK',
      { duration: 3000,
        panelClass: ['failed-snackbar']
      });
  }

}
