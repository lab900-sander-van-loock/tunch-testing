import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FridgeComponent } from './list/fridge.component';
import { FridgeDetailComponent } from './detail/fridge-detail.component';
import { FridgeUpdateComponent } from './update/fridge-update.component';
import { FridgeDeleteDialogComponent } from './delete/fridge-delete-dialog.component';
import { FridgeRoutingModule } from './route/fridge-routing.module';

@NgModule({
  imports: [SharedModule, FridgeRoutingModule],
  declarations: [FridgeComponent, FridgeDetailComponent, FridgeUpdateComponent, FridgeDeleteDialogComponent],
})
export class FridgeModule {}
