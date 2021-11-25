import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamille, getFamilleIdentifier } from '../famille.model';

export type EntityResponseType = HttpResponse<IFamille>;
export type EntityArrayResponseType = HttpResponse<IFamille[]>;

@Injectable({ providedIn: 'root' })
export class FamilleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/familles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(famille: IFamille): Observable<EntityResponseType> {
    return this.http.post<IFamille>(this.resourceUrl, famille, { observe: 'response' });
  }

  update(famille: IFamille): Observable<EntityResponseType> {
    return this.http.put<IFamille>(`${this.resourceUrl}/${getFamilleIdentifier(famille) as number}`, famille, { observe: 'response' });
  }

  partialUpdate(famille: IFamille): Observable<EntityResponseType> {
    return this.http.patch<IFamille>(`${this.resourceUrl}/${getFamilleIdentifier(famille) as number}`, famille, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFamille>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFamille[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFamilleToCollectionIfMissing(familleCollection: IFamille[], ...famillesToCheck: (IFamille | null | undefined)[]): IFamille[] {
    const familles: IFamille[] = famillesToCheck.filter(isPresent);
    if (familles.length > 0) {
      const familleCollectionIdentifiers = familleCollection.map(familleItem => getFamilleIdentifier(familleItem)!);
      const famillesToAdd = familles.filter(familleItem => {
        const familleIdentifier = getFamilleIdentifier(familleItem);
        if (familleIdentifier == null || familleCollectionIdentifiers.includes(familleIdentifier)) {
          return false;
        }
        familleCollectionIdentifiers.push(familleIdentifier);
        return true;
      });
      return [...famillesToAdd, ...familleCollection];
    }
    return familleCollection;
  }
}
