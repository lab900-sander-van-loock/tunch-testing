import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FridgeFormService, FridgeFormGroup } from './fridge-form.service';
import { IFridge } from '../fridge.model';
import { FridgeService } from '../service/fridge.service';

@Component({
  selector: 'jhi-fridge-update',
  templateUrl: './fridge-update.component.html',
})
export class FridgeUpdateComponent implements OnInit {
  isSaving = false;
  fridge: IFridge | null = null;

  editForm: FridgeFormGroup = this.fridgeFormService.createFridgeFormGroup();

  constructor(
    protected fridgeService: FridgeService,
    protected fridgeFormService: FridgeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fridge }) => {
      this.fridge = fridge;
      if (fridge) {
        this.updateForm(fridge);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fridge = this.fridgeFormService.getFridge(this.editForm);
    if (fridge.id !== null) {
      this.subscribeToSaveResponse(this.fridgeService.update(fridge));
    } else {
      this.subscribeToSaveResponse(this.fridgeService.create(fridge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFridge>>): void {
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

  protected updateForm(fridge: IFridge): void {
    this.fridge = fridge;
    this.fridgeFormService.resetForm(this.editForm, fridge);
  }
}
