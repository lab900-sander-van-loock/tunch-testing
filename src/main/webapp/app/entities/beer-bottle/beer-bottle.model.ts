import dayjs from 'dayjs/esm';
import { IBeer } from 'app/entities/beer/beer.model';
import { IFridge } from 'app/entities/fridge/fridge.model';

export interface IBeerBottle {
  id: string;
  expirationDate?: dayjs.Dayjs | null;
  beer?: Pick<IBeer, 'id'> | null;
  fridge?: Pick<IFridge, 'id'> | null;
}

export type NewBeerBottle = Omit<IBeerBottle, 'id'> & { id: null };
