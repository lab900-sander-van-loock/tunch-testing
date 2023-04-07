import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'beer',
        data: { pageTitle: 'tunchApp.beer.home.title' },
        loadChildren: () => import('./beer/beer.module').then(m => m.BeerModule),
      },
      {
        path: 'beer-bottle',
        data: { pageTitle: 'tunchApp.beerBottle.home.title' },
        loadChildren: () => import('./beer-bottle/beer-bottle.module').then(m => m.BeerBottleModule),
      },
      {
        path: 'fridge',
        data: { pageTitle: 'tunchApp.fridge.home.title' },
        loadChildren: () => import('./fridge/fridge.module').then(m => m.FridgeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
