import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CarComponent} from "./components/car/car.component";
import {LoginComponent} from "./components/admin-panel-login/login.component";
import {AdminPanelComponent} from "./components/admin-panel/admin-panel.component";
import {ErrorComponent} from "./components/error/error.component";
import {RegisterComponent} from "./components/register/register.component";
import {UserManagementComponent} from "./components/user-management/user-management.component";
import {AdminAuthorizeGuard} from "./service/guard/adminAuthorize.guard";
import {AddCarComponent} from "./components/add-car/add-car.component";
import {EditCarComponent} from "./components/edit-car/edit-car.component";
import {OperationsCarComponent} from "./components/operations-car/operations-car.component";
import {RentCarComponent} from "./components/rent-car/rent-car.component";
import {BuyCarComponent} from "./components/buy-car/buy-car.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {LostPasswordComponent} from "./components/admin-panel-login/lost-password/lost-password.component";
import {ConfirmAccountComponent} from "./components/register/confirm-account/confirm-account.component";
import {OrderStatsComponent} from "./components/order-stats/order-stats.component";

const routes: Routes = [
  { path: "", component: CarComponent},
  { path: "cars", component: CarComponent },
  { path: "register", component: RegisterComponent },
  { path: "login", component: LoginComponent },
  { path: 'profile', component: ProfileComponent},
  { path: "admin", component: AdminPanelComponent, canActivate: [AdminAuthorizeGuard] },
  { path: 'admin-dashboard', component: AdminPanelComponent, canActivate: [AdminAuthorizeGuard] },
  { path: 'admin-dashboard/users-management', component: UserManagementComponent, canActivate: [AdminAuthorizeGuard] },
  { path: 'admin-dashboard/car-add', component: AddCarComponent, canActivate: [AdminAuthorizeGuard] },
  { path: 'admin-dashboard/edit-car/:id', component: EditCarComponent, canActivate: [AdminAuthorizeGuard] },
  { path: 'admin-dashboard/operations-car', component: OperationsCarComponent, canActivate: [AdminAuthorizeGuard] },
  { path: 'admin-dashboard/statistics', component: OrderStatsComponent, canActivate: [AdminAuthorizeGuard] },
  { path: 'rent', component: RentCarComponent},
  { path: 'buy', component: BuyCarComponent},
  { path: 'lostPassword', component: LostPasswordComponent },
  { path: 'lostPassword/:hash', component: LostPasswordComponent },
  { path: 'confirm/:hash', component: ConfirmAccountComponent },
  { path: 'error', component: ErrorComponent },
  { path: '**', component: CarComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
