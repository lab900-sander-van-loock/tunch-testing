import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFridge, NewFridge } from '../fridge.model';

export type PartialUpdateFridge = Partial<IFridge> & Pick<IFridge, 'id'>;

export type EntityResponseType = HttpResponse<IFridge>;
export type EntityArrayResponseType = HttpResponse<IFridge[]>;

@Injectable({ providedIn: 'root' })
export class FridgeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fridges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fridge: NewFridge): Observable<EntityResponseType> {
    return this.http.post<IFridge>(this.resourceUrl, fridge, { observe: 'response' });
  }

  update(fridge: IFridge): Observable<EntityResponseType> {
    return this.http.put<IFridge>(`${this.resourceUrl}/${this.getFridgeIdentifier(fridge)}`, fridge, { observe: 'response' });
  }

  partialUpdate(fridge: PartialUpdateFridge): Observable<EntityResponseType> {
    return this.http.patch<IFridge>(`${this.resourceUrl}/${this.getFridgeIdentifier(fridge)}`, fridge, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFridge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFridge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFridgeIdentifier(fridge: Pick<IFridge, 'id'>): number {
    return fridge.id;
  }

  compareFridge(o1: Pick<IFridge, 'id'> | null, o2: Pick<IFridge, 'id'> | null): boolean {
    return o1 && o2 ? this.getFridgeIdentifier(o1) === this.getFridgeIdentifier(o2) : o1 === o2;
  }

  addFridgeToCollectionIfMissing<Type extends Pick<IFridge, 'id'>>(
    fridgeCollection: Type[],
    ...fridgesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fridges: Type[] = fridgesToCheck.filter(isPresent);
    if (fridges.length > 0) {
      const fridgeCollectionIdentifiers = fridgeCollection.map(fridgeItem => this.getFridgeIdentifier(fridgeItem)!);
      const fridgesToAdd = fridges.filter(fridgeItem => {
        const fridgeIdentifier = this.getFridgeIdentifier(fridgeItem);
        if (fridgeCollectionIdentifiers.includes(fridgeIdentifier)) {
          return false;
        }
        fridgeCollectionIdentifiers.push(fridgeIdentifier);
        return true;
      });
      return [...fridgesToAdd, ...fridgeCollection];
    }
    return fridgeCollection;
  }
}
