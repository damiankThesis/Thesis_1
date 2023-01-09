import { Component, OnInit } from '@angular/core';
import {Car} from "../../model/car.model";
import {CarService} from "../../service/car/car.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UploadService} from "../../service/upload/upload.service";

@Component({
  selector: 'app-edit-car',
  templateUrl: './edit-car.component.html',
  styleUrls: ['./edit-car.component.scss']
})
export class EditCarComponent implements OnInit {

  car!: Car;
  carEditForm!: FormGroup;
  requiredTypes = "image/jpeg, image/png";
  imageForm!: FormGroup;
  image!: string | null;

  constructor(
    private carService: CarService,
    private uploadService: UploadService,
    private router: ActivatedRoute,
    private router2: Router,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    this.getCar();
    this.createCarEditForm();
    this.createImageForm();
  }

  private createCarEditForm() {
    this.carEditForm = this.formBuilder.group({
      brand: ["", Validators.required],
      model: ["", Validators.required],
      registration: ["", Validators.required],
      buyPrice: ["", Validators.required],
      rentBasePrice: ["", Validators.required],
      availableStatus: ["", Validators.required],
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
      description: [""]
    });
  }

  private createImageForm() {
    this.imageForm = this.formBuilder.group({
      file: ['']
    })
  }

  public getCar() {
    let id = Number(this.router.snapshot.params['id']);
    this.carService.getEditCar(id).subscribe({
      next: (car) => {
        this.carEditForm.setValue({
          brand: car.brand,
          model: car.model,
          registration: car.registration,
          buyPrice: car.buyPrice,
          rentBasePrice: car.rentBasePrice,
          availableStatus: car.availableStatus,
          type: car.carDetail.type,
          productionYear: car.carDetail.productionYear,
          fuel: car.carDetail.fuel,
          power: car.carDetail.power,
          capacity: car.carDetail.capacity,
          gearbox: car.carDetail.gearbox,
          drive: car.carDetail.drive,
          doors: car.carDetail.doors,
          seats: car.carDetail.seats,
          color: car.carDetail.color,
          description: car.carDetail.description
        });
        this.image = car.image
      },
      error: () => console.log("error")
    });

  }

  editCar() {
    let id = Number(this.router.snapshot.params['id']);
    if(this.carEditForm.valid) {
      this.car = this.carEditForm.value;
      if (this.image != null) this.car.image = this.image;

      this.carService.editCar(id, this.carEditForm.value)
        .subscribe({
          next: (response: any) => {
            if(response) {
              this.snackBar.open('Car was successfully edited!', 'OK',
                { duration: 3000,
                  panelClass: ['success-snackbar']
              });
            }
          },
          error: () => {
            this.snackBar.open('Car has not been edited!', 'OK',
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

  upload() {
    let formData = new FormData();
    formData.append('file', this.imageForm.get('file')?.value);

    this.uploadService.uploadImage(formData)
      .subscribe( result => {
        this.image = result.filename
      });
  }

  onFileChange(event: any) {
    if (event.target.files) {
      this.imageForm.patchValue({
        file: event.target.files[0]
      });
    }
  }

}
