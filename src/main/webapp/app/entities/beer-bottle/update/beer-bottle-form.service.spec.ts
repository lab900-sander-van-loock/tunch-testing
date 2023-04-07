import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../beer-bottle.test-samples';

import { BeerBottleFormService } from './beer-bottle-form.service';

describe('BeerBottle Form Service', () => {
  let service: BeerBottleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeerBottleFormService);
  });

  describe('Service methods', () => {
    describe('createBeerBottleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBeerBottleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            expirationDate: expect.any(Object),
            beer: expect.any(Object),
            fridge: expect.any(Object),
          })
        );
      });

      it('passing IBeerBottle should create a new form with FormGroup', () => {
        const formGroup = service.createBeerBottleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            expirationDate: expect.any(Object),
            beer: expect.any(Object),
            fridge: expect.any(Object),
          })
        );
      });
    });

    describe('getBeerBottle', () => {
      it('should return NewBeerBottle for default BeerBottle initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBeerBottleFormGroup(sampleWithNewData);

        const beerBottle = service.getBeerBottle(formGroup) as any;

        expect(beerBottle).toMatchObject(sampleWithNewData);
      });

      it('should return NewBeerBottle for empty BeerBottle initial value', () => {
        const formGroup = service.createBeerBottleFormGroup();

        const beerBottle = service.getBeerBottle(formGroup) as any;

        expect(beerBottle).toMatchObject({});
      });

      it('should return IBeerBottle', () => {
        const formGroup = service.createBeerBottleFormGroup(sampleWithRequiredData);

        const beerBottle = service.getBeerBottle(formGroup) as any;

        expect(beerBottle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBeerBottle should not enable id FormControl', () => {
        const formGroup = service.createBeerBottleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBeerBottle should disable id FormControl', () => {
        const formGroup = service.createBeerBottleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
