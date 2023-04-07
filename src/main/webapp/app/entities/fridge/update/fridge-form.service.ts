import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFridge, NewFridge } from '../fridge.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFridge for edit and NewFridgeFormGroupInput for create.
 */
type FridgeFormGroupInput = IFridge | PartialWithRequiredKeyOf<NewFridge>;

type FridgeFormDefaults = Pick<NewFridge, 'id'>;

type FridgeFormGroupContent = {
  id: FormControl<IFridge['id'] | NewFridge['id']>;
  name: FormControl<IFridge['name']>;
  location: FormControl<IFridge['location']>;
};

export type FridgeFormGroup = FormGroup<FridgeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FridgeFormService {
  createFridgeFormGroup(fridge: FridgeFormGroupInput = { id: null }): FridgeFormGroup {
    const fridgeRawValue = {
      ...this.getFormDefaults(),
      ...fridge,
    };
    return new FormGroup<FridgeFormGroupContent>({
      id: new FormControl(
        { value: fridgeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(fridgeRawValue.name, {
        validators: [Validators.required],
      }),
      location: new FormControl(fridgeRawValue.location),
    });
  }

  getFridge(form: FridgeFormGroup): IFridge | NewFridge {
    return form.getRawValue() as IFridge | NewFridge;
  }

  resetForm(form: FridgeFormGroup, fridge: FridgeFormGroupInput): void {
    const fridgeRawValue = { ...this.getFormDefaults(), ...fridge };
    form.reset(
      {
        ...fridgeRawValue,
        id: { value: fridgeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FridgeFormDefaults {
    return {
      id: null,
    };
  }
}
