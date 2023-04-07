import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeerBottle, NewBeerBottle } from '../beer-bottle.model';

export type PartialUpdateBeerBottle = Partial<IBeerBottle> & Pick<IBeerBottle, 'id'>;

type RestOf<T extends IBeerBottle | NewBeerBottle> = Omit<T, 'expirationDate'> & {
  expirationDate?: string | null;
};

export type RestBeerBottle = RestOf<IBeerBottle>;

export type NewRestBeerBottle = RestOf<NewBeerBottle>;

export type PartialUpdateRestBeerBottle = RestOf<PartialUpdateBeerBottle>;

export type EntityResponseType = HttpResponse<IBeerBottle>;
export type EntityArrayResponseType = HttpResponse<IBeerBottle[]>;

@Injectable({ providedIn: 'root' })
export class BeerBottleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beer-bottles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beerBottle: NewBeerBottle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beerBottle);
    return this.http
      .post<RestBeerBottle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(beerBottle: IBeerBottle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beerBottle);
    return this.http
      .put<RestBeerBottle>(`${this.resourceUrl}/${this.getBeerBottleIdentifier(beerBottle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(beerBottle: PartialUpdateBeerBottle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(beerBottle);
    return this.http
      .patch<RestBeerBottle>(`${this.resourceUrl}/${this.getBeerBottleIdentifier(beerBottle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestBeerBottle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBeerBottle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBeerBottleIdentifier(beerBottle: Pick<IBeerBottle, 'id'>): string {
    return beerBottle.id;
  }

  compareBeerBottle(o1: Pick<IBeerBottle, 'id'> | null, o2: Pick<IBeerBottle, 'id'> | null): boolean {
    return o1 && o2 ? this.getBeerBottleIdentifier(o1) === this.getBeerBottleIdentifier(o2) : o1 === o2;
  }

  addBeerBottleToCollectionIfMissing<Type extends Pick<IBeerBottle, 'id'>>(
    beerBottleCollection: Type[],
    ...beerBottlesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const beerBottles: Type[] = beerBottlesToCheck.filter(isPresent);
    if (beerBottles.length > 0) {
      const beerBottleCollectionIdentifiers = beerBottleCollection.map(beerBottleItem => this.getBeerBottleIdentifier(beerBottleItem)!);
      const beerBottlesToAdd = beerBottles.filter(beerBottleItem => {
        const beerBottleIdentifier = this.getBeerBottleIdentifier(beerBottleItem);
        if (beerBottleCollectionIdentifiers.includes(beerBottleIdentifier)) {
          return false;
        }
        beerBottleCollectionIdentifiers.push(beerBottleIdentifier);
        return true;
      });
      return [...beerBottlesToAdd, ...beerBottleCollection];
    }
    return beerBottleCollection;
  }

  protected convertDateFromClient<T extends IBeerBottle | NewBeerBottle | PartialUpdateBeerBottle>(beerBottle: T): RestOf<T> {
    return {
      ...beerBottle,
      expirationDate: beerBottle.expirationDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBeerBottle: RestBeerBottle): IBeerBottle {
    return {
      ...restBeerBottle,
      expirationDate: restBeerBottle.expirationDate ? dayjs(restBeerBottle.expirationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBeerBottle>): HttpResponse<IBeerBottle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBeerBottle[]>): HttpResponse<IBeerBottle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
