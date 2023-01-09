import {User} from "./user.model";
import {Car} from "./car.model";

export interface RentModel {
  id: number;
  startDate: Date;
  endDate: Date;
  rentDate: Date;
  description: string;
  totalCost: number,
  user: User,
  car: Car
  payment: string;
}
