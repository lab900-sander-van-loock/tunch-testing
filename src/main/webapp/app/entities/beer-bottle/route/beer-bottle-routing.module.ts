import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BeerBottleComponent } from '../list/beer-bottle.component';
import { BeerBottleDetailComponent } from '../detail/beer-bottle-detail.component';
import { BeerBottleUpdateComponent } from '../update/beer-bottle-update.component';
import { BeerBottleRoutingResolveService } from './beer-bottle-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const beerBottleRoute: Routes = [
  {
    path: '',
    component: BeerBottleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BeerBottleDetailComponent,
    resolve: {
      beerBottle: BeerBottleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BeerBottleUpdateComponent,
    resolve: {
      beerBottle: BeerBottleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BeerBottleUpdateComponent,
    resolve: {
      beerBottle: BeerBottleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(beerBottleRoute)],
  exports: [RouterModule],
})
export class BeerBottleRoutingModule {}
