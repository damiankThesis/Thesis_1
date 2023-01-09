import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CarComponent } from './components/car/car.component';
import { CarService } from "./service/car/car.service";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { FooterComponent } from "./components/footer/footer.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NavbarComponent } from "./components/navbar/navbar.component";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatNativeDateModule } from "@angular/material/core";
import { NgxMatDatetimePickerModule, NgxMatTimepickerModule } from "@angular-material-components/datetime-picker";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { LoginComponent } from "./components/admin-panel-login/login.component";
import { AdminPanelComponent } from "./components/admin-panel/admin-panel.component";
import { MatDialogModule } from "@angular/material/dialog";
import { ErrorComponent } from './components/error/error.component';
import { FlexModule } from "@angular/flex-layout";
import { SidebarComponent } from "./components/sidebar/sidebar.component";
import { MatListModule } from "@angular/material/list";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RegisterComponent } from "./components/register/register.component";
import { UserManagementComponent } from './components/user-management/user-management.component';
import { UserComponent } from './components/user/user.component';
import { JwtInterceptor } from "./service/jwt/jwt.interceptor";
import { AdminAuthorizeGuard } from "./service/guard/adminAuthorize.guard";
import { AddCarComponent } from './components/add-car/add-car.component';
import { EditCarComponent } from './components/edit-car/edit-car.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { OperationsCarComponent } from './components/operations-car/operations-car.component';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { MatIconModule } from "@angular/material/icon";
import { RentCarComponent } from './components/rent-car/rent-car.component';
import { BuyCarComponent } from './components/buy-car/buy-car.component';
import { MatTooltipModule} from "@angular/material/tooltip";
import { RentSummaryComponent } from "./components/rent-car/rent-summary/rent-summary.component";
import { MatRadioModule } from "@angular/material/radio";
import { MatTableModule } from "@angular/material/table";
import { ProfileComponent } from './components/profile/profile.component';
import { MatPaginatorModule } from "@angular/material/paginator";
import { BuySummaryComponent } from "./components/buy-car/buy-summary/buy-summary.component";
import { MatSortModule } from "@angular/material/sort";
import { OperationRentDetailsComponent } from "./components/operations-car/operation-rent/operation-details/operation-rent-details.component";
import { OperationsRentComponent } from "./components/operations-car/operation-rent/operations-rent.component";
import { OperationsBuyComponent } from "./components/operations-car/operation-buy/operations-buy.component";
import { OperationBuyDetailsComponent } from "./components/operations-car/operation-buy/operation-details/operation-buy-details.component";
import { LostPasswordComponent } from './components/admin-panel-login/lost-password/lost-password.component';
import { ConfirmAccountComponent } from './components/register/confirm-account/confirm-account.component';
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { CarHistoryComponent } from './components/car/car-history/car-history.component';
import { OrderStatsComponent } from './components/order-stats/order-stats.component';
import { MatSliderModule } from "@angular/material/slider";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { OperationEditComponent } from './components/operations-car/operation-edit/operation-edit.component';
import { UserEditComponent } from './components/user/user-edit/user-edit.component';

@NgModule({
  declarations: [
    AppComponent,
    CarComponent,
    FooterComponent,
    NavbarComponent,
    LoginComponent,
    AdminPanelComponent,
    ErrorComponent,
    SidebarComponent,
    RegisterComponent,
    UserManagementComponent,
    UserComponent,
    AddCarComponent,
    EditCarComponent,
    OperationsCarComponent,
    ConfirmDialogComponent,
    RentCarComponent,
    BuyCarComponent,
    RentSummaryComponent,
    ProfileComponent,
    BuySummaryComponent,
    OperationRentDetailsComponent,
    OperationsRentComponent,
    OperationsBuyComponent,
    OperationBuyDetailsComponent,
    LostPasswordComponent,
    ConfirmAccountComponent,
    CarHistoryComponent,
    OrderStatsComponent,
    OperationEditComponent,
    UserEditComponent,
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatDatepickerModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatNativeDateModule,
        NgxMatDatetimePickerModule,
        NgxMatTimepickerModule,
        MatInputModule,
        MatButtonModule,
        MatDialogModule,
        FlexModule,
        MatListModule,
        NgbModule,
        MatSnackBarModule,
        MatIconModule,
        MatTooltipModule,
        MatRadioModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatProgressSpinnerModule,
        FormsModule,
        MatSliderModule,
        MatCheckboxModule
    ],
  providers: [
    CarService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    AdminAuthorizeGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
