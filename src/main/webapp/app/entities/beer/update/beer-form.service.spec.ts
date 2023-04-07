import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../beer.test-samples';

import { BeerFormService } from './beer-form.service';

describe('Beer Form Service', () => {
  let service: BeerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeerFormService);
  });

  describe('Service methods', () => {
    describe('createBeerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBeerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            brewery: expect.any(Object),
            percentage: expect.any(Object),
          })
        );
      });

      it('passing IBeer should create a new form with FormGroup', () => {
        const formGroup = service.createBeerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            brewery: expect.any(Object),
            percentage: expect.any(Object),
          })
        );
      });
    });

    describe('getBeer', () => {
      it('should return NewBeer for default Beer initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBeerFormGroup(sampleWithNewData);

        const beer = service.getBeer(formGroup) as any;

        expect(beer).toMatchObject(sampleWithNewData);
      });

      it('should return NewBeer for empty Beer initial value', () => {
        const formGroup = service.createBeerFormGroup();

        const beer = service.getBeer(formGroup) as any;

        expect(beer).toMatchObject({});
      });

      it('should return IBeer', () => {
        const formGroup = service.createBeerFormGroup(sampleWithRequiredData);

        const beer = service.getBeer(formGroup) as any;

        expect(beer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBeer should not enable id FormControl', () => {
        const formGroup = service.createBeerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBeer should disable id FormControl', () => {
        const formGroup = service.createBeerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
