import { IUser } from 'app/shared/model/user.model';
import { ICar } from 'app/shared/model/car.model';

export interface IActiveRent {
  id?: number;
  duration?: number;
  client?: IUser;
  car?: ICar;
}

export const defaultValue: Readonly<IActiveRent> = {};
