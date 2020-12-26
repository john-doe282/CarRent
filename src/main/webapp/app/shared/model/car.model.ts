import { IModel } from 'app/shared/model/model.model';
import { IType } from 'app/shared/model/type.model';
import { ILocation } from 'app/shared/model/location.model';
import { IUser } from 'app/shared/model/user.model';
import { CarStatus } from 'app/shared/model/enumerations/car-status.model';

export interface ICar {
  id?: number;
  description?: string;
  status?: CarStatus;
  pricePerHour?: number;
  model?: IModel;
  type?: IType;
  location?: ILocation;
  owner?: IUser;
}

export const defaultValue: Readonly<ICar> = {};
