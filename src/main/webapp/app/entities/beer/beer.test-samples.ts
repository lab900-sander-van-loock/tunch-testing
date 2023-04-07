import { IBeer, NewBeer } from './beer.model';

export const sampleWithRequiredData: IBeer = {
  id: 27242,
  name: 'scalable Pants Drive',
  percentage: 89633,
};

export const sampleWithPartialData: IBeer = {
  id: 76573,
  name: 'override Silver portal',
  brewery: 'expedite',
  percentage: 7899,
};

export const sampleWithFullData: IBeer = {
  id: 39978,
  name: 'navigating',
  brewery: 'Fresh Account cutting-edge',
  percentage: 61529,
};

export const sampleWithNewData: NewBeer = {
  name: 'Parks maximize Ergonomic',
  percentage: 6927,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
