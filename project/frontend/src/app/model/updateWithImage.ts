import {CarDetails} from "./carDetails.model";

export interface UpdateWithImage {
  id: number;
  brand: string;
  model: string;
  registration: string;
  availableStatus: boolean;
  buyPrice: number,
  rentBasePrice: number,
  carDetail: CarDetails;
  image: string
}
