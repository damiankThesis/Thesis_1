import {User} from "./user.model";
import {Car} from "./car.model";

export interface BuyModel {
  id: number;
  meetDate: Date;
  reqDate: Date;
  description: string;
  totalBuyCost: number;
  user: User;
  car: Car;
}
