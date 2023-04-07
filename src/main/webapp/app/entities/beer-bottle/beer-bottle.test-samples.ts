import dayjs from 'dayjs/esm';

import { IBeerBottle, NewBeerBottle } from './beer-bottle.model';

export const sampleWithRequiredData: IBeerBottle = {
  id: '57134dbb-b351-44e9-b013-b45355c58fc9',
  expirationDate: dayjs('2023-04-07'),
};

export const sampleWithPartialData: IBeerBottle = {
  id: 'bf2bf7e0-6b69-4ab2-a443-e811db25cbc9',
  expirationDate: dayjs('2023-04-06'),
};

export const sampleWithFullData: IBeerBottle = {
  id: 'fb3aaa86-0571-4bc2-9808-2c8190e57f07',
  expirationDate: dayjs('2023-04-07'),
};

export const sampleWithNewData: NewBeerBottle = {
  expirationDate: dayjs('2023-04-07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
