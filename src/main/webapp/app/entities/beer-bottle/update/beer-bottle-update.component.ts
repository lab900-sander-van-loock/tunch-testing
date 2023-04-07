import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BeerBottleFormService, BeerBottleFormGroup } from './beer-bottle-form.service';
import { IBeerBottle } from '../beer-bottle.model';
import { BeerBottleService } from '../service/beer-bottle.service';
import { IBeer } from 'app/entities/beer/beer.model';
import { BeerService } from 'app/entities/beer/service/beer.service';
import { IFridge } from 'app/entities/fridge/fridge.model';
import { FridgeService } from 'app/entities/fridge/service/fridge.service';

@Component({
  selector: 'jhi-beer-bottle-update',
  templateUrl: './beer-bottle-update.component.html',
})
export class BeerBottleUpdateComponent implements OnInit {
  isSaving = false;
  beerBottle: IBeerBottle | null = null;

  beersSharedCollection: IBeer[] = [];
  fridgesSharedCollection: IFridge[] = [];

  editForm: BeerBottleFormGroup = this.beerBottleFormService.createBeerBottleFormGroup();

  constructor(
    protected beerBottleService: BeerBottleService,
    protected beerBottleFormService: BeerBottleFormService,
    protected beerService: BeerService,
    protected fridgeService: FridgeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBeer = (o1: IBeer | null, o2: IBeer | null): boolean => this.beerService.compareBeer(o1, o2);

  compareFridge = (o1: IFridge | null, o2: IFridge | null): boolean => this.fridgeService.compareFridge(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beerBottle }) => {
      this.beerBottle = beerBottle;
      if (beerBottle) {
        this.updateForm(beerBottle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const beerBottle = this.beerBottleFormService.getBeerBottle(this.editForm);
    if (beerBottle.id !== null) {
      this.subscribeToSaveResponse(this.beerBottleService.update(beerBottle));
    } else {
      this.subscribeToSaveResponse(this.beerBottleService.create(beerBottle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeerBottle>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(beerBottle: IBeerBottle): void {
    this.beerBottle = beerBottle;
    this.beerBottleFormService.resetForm(this.editForm, beerBottle);

    this.beersSharedCollection = this.beerService.addBeerToCollectionIfMissing<IBeer>(this.beersSharedCollection, beerBottle.beer);
    this.fridgesSharedCollection = this.fridgeService.addFridgeToCollectionIfMissing<IFridge>(
      this.fridgesSharedCollection,
      beerBottle.fridge
    );
  }

  protected loadRelationshipsOptions(): void {
    this.beerService
      .query()
      .pipe(map((res: HttpResponse<IBeer[]>) => res.body ?? []))
      .pipe(map((beers: IBeer[]) => this.beerService.addBeerToCollectionIfMissing<IBeer>(beers, this.beerBottle?.beer)))
      .subscribe((beers: IBeer[]) => (this.beersSharedCollection = beers));

    this.fridgeService
      .query()
      .pipe(map((res: HttpResponse<IFridge[]>) => res.body ?? []))
      .pipe(map((fridges: IFridge[]) => this.fridgeService.addFridgeToCollectionIfMissing<IFridge>(fridges, this.beerBottle?.fridge)))
      .subscribe((fridges: IFridge[]) => (this.fridgesSharedCollection = fridges));
  }
}
