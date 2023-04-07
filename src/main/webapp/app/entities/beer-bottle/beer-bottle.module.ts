import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BeerBottleComponent } from './list/beer-bottle.component';
import { BeerBottleDetailComponent } from './detail/beer-bottle-detail.component';
import { BeerBottleUpdateComponent } from './update/beer-bottle-update.component';
import { BeerBottleDeleteDialogComponent } from './delete/beer-bottle-delete-dialog.component';
import { BeerBottleRoutingModule } from './route/beer-bottle-routing.module';

@NgModule({
  imports: [SharedModule, BeerBottleRoutingModule],
  declarations: [BeerBottleComponent, BeerBottleDetailComponent, BeerBottleUpdateComponent, BeerBottleDeleteDialogComponent],
})
export class BeerBottleModule {}
