import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBeerBottle } from '../beer-bottle.model';
import { BeerBottleService } from '../service/beer-bottle.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './beer-bottle-delete-dialog.component.html',
})
export class BeerBottleDeleteDialogComponent {
  beerBottle?: IBeerBottle;

  constructor(protected beerBottleService: BeerBottleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.beerBottleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
