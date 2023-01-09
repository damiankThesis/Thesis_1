import {CarDetails} from "./carDetails.model";

export interface Car {
  id: number,
  brand: string,
  model: string,
  registration: string,
  availableStatus: boolean,
  buyPrice: number,
  rentBasePrice: number,
  image: string,
  carDetail: CarDetails
}
