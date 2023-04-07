import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBeerBottle } from '../beer-bottle.model';
import { BeerBottleService } from '../service/beer-bottle.service';

@Injectable({ providedIn: 'root' })
export class BeerBottleRoutingResolveService implements Resolve<IBeerBottle | null> {
  constructor(protected service: BeerBottleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBeerBottle | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((beerBottle: HttpResponse<IBeerBottle>) => {
          if (beerBottle.body) {
            return of(beerBottle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
