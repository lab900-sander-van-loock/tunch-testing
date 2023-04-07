import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBeer, NewBeer } from '../beer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBeer for edit and NewBeerFormGroupInput for create.
 */
type BeerFormGroupInput = IBeer | PartialWithRequiredKeyOf<NewBeer>;

type BeerFormDefaults = Pick<NewBeer, 'id'>;

type BeerFormGroupContent = {
  id: FormControl<IBeer['id'] | NewBeer['id']>;
  name: FormControl<IBeer['name']>;
  brewery: FormControl<IBeer['brewery']>;
  percentage: FormControl<IBeer['percentage']>;
};

export type BeerFormGroup = FormGroup<BeerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BeerFormService {
  createBeerFormGroup(beer: BeerFormGroupInput = { id: null }): BeerFormGroup {
    const beerRawValue = {
      ...this.getFormDefaults(),
      ...beer,
    };
    return new FormGroup<BeerFormGroupContent>({
      id: new FormControl(
        { value: beerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(beerRawValue.name, {
        validators: [Validators.required],
      }),
      brewery: new FormControl(beerRawValue.brewery),
      percentage: new FormControl(beerRawValue.percentage, {
        validators: [Validators.required],
      }),
    });
  }

  getBeer(form: BeerFormGroup): IBeer | NewBeer {
    return form.getRawValue() as IBeer | NewBeer;
  }

  resetForm(form: BeerFormGroup, beer: BeerFormGroupInput): void {
    const beerRawValue = { ...this.getFormDefaults(), ...beer };
    form.reset(
      {
        ...beerRawValue,
        id: { value: beerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BeerFormDefaults {
    return {
      id: null,
    };
  }
}
