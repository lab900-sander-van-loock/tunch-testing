import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fridge.test-samples';

import { FridgeFormService } from './fridge-form.service';

describe('Fridge Form Service', () => {
  let service: FridgeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FridgeFormService);
  });

  describe('Service methods', () => {
    describe('createFridgeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFridgeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            location: expect.any(Object),
          })
        );
      });

      it('passing IFridge should create a new form with FormGroup', () => {
        const formGroup = service.createFridgeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            location: expect.any(Object),
          })
        );
      });
    });

    describe('getFridge', () => {
      it('should return NewFridge for default Fridge initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFridgeFormGroup(sampleWithNewData);

        const fridge = service.getFridge(formGroup) as any;

        expect(fridge).toMatchObject(sampleWithNewData);
      });

      it('should return NewFridge for empty Fridge initial value', () => {
        const formGroup = service.createFridgeFormGroup();

        const fridge = service.getFridge(formGroup) as any;

        expect(fridge).toMatchObject({});
      });

      it('should return IFridge', () => {
        const formGroup = service.createFridgeFormGroup(sampleWithRequiredData);

        const fridge = service.getFridge(formGroup) as any;

        expect(fridge).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFridge should not enable id FormControl', () => {
        const formGroup = service.createFridgeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFridge should disable id FormControl', () => {
        const formGroup = service.createFridgeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
