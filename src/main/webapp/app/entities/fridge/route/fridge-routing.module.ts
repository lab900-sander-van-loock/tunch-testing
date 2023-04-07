import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FridgeComponent } from '../list/fridge.component';
import { FridgeDetailComponent } from '../detail/fridge-detail.component';
import { FridgeUpdateComponent } from '../update/fridge-update.component';
import { FridgeRoutingResolveService } from './fridge-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fridgeRoute: Routes = [
  {
    path: '',
    component: FridgeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FridgeDetailComponent,
    resolve: {
      fridge: FridgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FridgeUpdateComponent,
    resolve: {
      fridge: FridgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FridgeUpdateComponent,
    resolve: {
      fridge: FridgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fridgeRoute)],
  exports: [RouterModule],
})
export class FridgeRoutingModule {}
