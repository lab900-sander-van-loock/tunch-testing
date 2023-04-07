import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeer, NewBeer } from '../beer.model';

export type PartialUpdateBeer = Partial<IBeer> & Pick<IBeer, 'id'>;

export type EntityResponseType = HttpResponse<IBeer>;
export type EntityArrayResponseType = HttpResponse<IBeer[]>;

@Injectable({ providedIn: 'root' })
export class BeerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beer: NewBeer): Observable<EntityResponseType> {
    return this.http.post<IBeer>(this.resourceUrl, beer, { observe: 'response' });
  }

  update(beer: IBeer): Observable<EntityResponseType> {
    return this.http.put<IBeer>(`${this.resourceUrl}/${this.getBeerIdentifier(beer)}`, beer, { observe: 'response' });
  }

  partialUpdate(beer: PartialUpdateBeer): Observable<EntityResponseType> {
    return this.http.patch<IBeer>(`${this.resourceUrl}/${this.getBeerIdentifier(beer)}`, beer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBeer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBeer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBeerIdentifier(beer: Pick<IBeer, 'id'>): number {
    return beer.id;
  }

  compareBeer(o1: Pick<IBeer, 'id'> | null, o2: Pick<IBeer, 'id'> | null): boolean {
    return o1 && o2 ? this.getBeerIdentifier(o1) === this.getBeerIdentifier(o2) : o1 === o2;
  }

  addBeerToCollectionIfMissing<Type extends Pick<IBeer, 'id'>>(
    beerCollection: Type[],
    ...beersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const beers: Type[] = beersToCheck.filter(isPresent);
    if (beers.length > 0) {
      const beerCollectionIdentifiers = beerCollection.map(beerItem => this.getBeerIdentifier(beerItem)!);
      const beersToAdd = beers.filter(beerItem => {
        const beerIdentifier = this.getBeerIdentifier(beerItem);
        if (beerCollectionIdentifiers.includes(beerIdentifier)) {
          return false;
        }
        beerCollectionIdentifiers.push(beerIdentifier);
        return true;
      });
      return [...beersToAdd, ...beerCollection];
    }
    return beerCollection;
  }
}
