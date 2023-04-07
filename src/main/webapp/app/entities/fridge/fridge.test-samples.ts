import { IFridge, NewFridge } from './fridge.model';

export const sampleWithRequiredData: IFridge = {
  id: 87760,
  name: 'e-business attitude bandwidth',
};

export const sampleWithPartialData: IFridge = {
  id: 66604,
  name: 'Handcrafted copying',
};

export const sampleWithFullData: IFridge = {
  id: 13808,
  name: 'Fresh',
  location: 'Massachusetts',
};

export const sampleWithNewData: NewFridge = {
  name: 'Graphical holistic Spur',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
