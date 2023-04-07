import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBeerBottle } from '../beer-bottle.model';

@Component({
  selector: 'jhi-beer-bottle-detail',
  templateUrl: './beer-bottle-detail.component.html',
})
export class BeerBottleDetailComponent implements OnInit {
  beerBottle: IBeerBottle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beerBottle }) => {
      this.beerBottle = beerBottle;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
