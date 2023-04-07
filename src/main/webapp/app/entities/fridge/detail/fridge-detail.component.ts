import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFridge } from '../fridge.model';

@Component({
  selector: 'jhi-fridge-detail',
  templateUrl: './fridge-detail.component.html',
})
export class FridgeDetailComponent implements OnInit {
  fridge: IFridge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fridge }) => {
      this.fridge = fridge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
