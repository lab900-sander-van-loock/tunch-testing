import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBeerBottle, NewBeerBottle } from '../beer-bottle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBeerBottle for edit and NewBeerBottleFormGroupInput for create.
 */
type BeerBottleFormGroupInput = IBeerBottle | PartialWithRequiredKeyOf<NewBeerBottle>;

type BeerBottleFormDefaults = Pick<NewBeerBottle, 'id'>;

type BeerBottleFormGroupContent = {
  id: FormControl<IBeerBottle['id'] | NewBeerBottle['id']>;
  expirationDate: FormControl<IBeerBottle['expirationDate']>;
  beer: FormControl<IBeerBottle['beer']>;
  fridge: FormControl<IBeerBottle['fridge']>;
};

export type BeerBottleFormGroup = FormGroup<BeerBottleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BeerBottleFormService {
  createBeerBottleFormGroup(beerBottle: BeerBottleFormGroupInput = { id: null }): BeerBottleFormGroup {
    const beerBottleRawValue = {
      ...this.getFormDefaults(),
      ...beerBottle,
    };
    return new FormGroup<BeerBottleFormGroupContent>({
      id: new FormControl(
        { value: beerBottleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      expirationDate: new FormControl(beerBottleRawValue.expirationDate, {
        validators: [Validators.required],
      }),
      beer: new FormControl(beerBottleRawValue.beer),
      fridge: new FormControl(beerBottleRawValue.fridge),
    });
  }

  getBeerBottle(form: BeerBottleFormGroup): IBeerBottle | NewBeerBottle {
    return form.getRawValue() as IBeerBottle | NewBeerBottle;
  }

  resetForm(form: BeerBottleFormGroup, beerBottle: BeerBottleFormGroupInput): void {
    const beerBottleRawValue = { ...this.getFormDefaults(), ...beerBottle };
    form.reset(
      {
        ...beerBottleRawValue,
        id: { value: beerBottleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BeerBottleFormDefaults {
    return {
      id: null,
    };
  }
}
