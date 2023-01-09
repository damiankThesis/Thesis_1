import { ResponseModel } from "./responseModel";

export interface ListResponseModel<T> extends ResponseModel{
  body:T[];
}
