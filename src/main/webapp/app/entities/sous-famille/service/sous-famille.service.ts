import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousFamille, getSousFamilleIdentifier } from '../sous-famille.model';

export type EntityResponseType = HttpResponse<ISousFamille>;
export type EntityArrayResponseType = HttpResponse<ISousFamille[]>;

@Injectable({ providedIn: 'root' })
export class SousFamilleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-familles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousFamille: ISousFamille): Observable<EntityResponseType> {
    return this.http.post<ISousFamille>(this.resourceUrl, sousFamille, { observe: 'response' });
  }

  update(sousFamille: ISousFamille): Observable<EntityResponseType> {
    return this.http.put<ISousFamille>(`${this.resourceUrl}/${getSousFamilleIdentifier(sousFamille) as number}`, sousFamille, {
      observe: 'response',
    });
  }

  partialUpdate(sousFamille: ISousFamille): Observable<EntityResponseType> {
    return this.http.patch<ISousFamille>(`${this.resourceUrl}/${getSousFamilleIdentifier(sousFamille) as number}`, sousFamille, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousFamille>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousFamille[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousFamilleToCollectionIfMissing(
    sousFamilleCollection: ISousFamille[],
    ...sousFamillesToCheck: (ISousFamille | null | undefined)[]
  ): ISousFamille[] {
    const sousFamilles: ISousFamille[] = sousFamillesToCheck.filter(isPresent);
    if (sousFamilles.length > 0) {
      const sousFamilleCollectionIdentifiers = sousFamilleCollection.map(sousFamilleItem => getSousFamilleIdentifier(sousFamilleItem)!);
      const sousFamillesToAdd = sousFamilles.filter(sousFamilleItem => {
        const sousFamilleIdentifier = getSousFamilleIdentifier(sousFamilleItem);
        if (sousFamilleIdentifier == null || sousFamilleCollectionIdentifiers.includes(sousFamilleIdentifier)) {
          return false;
        }
        sousFamilleCollectionIdentifiers.push(sousFamilleIdentifier);
        return true;
      });
      return [...sousFamillesToAdd, ...sousFamilleCollection];
    }
    return sousFamilleCollection;
  }
}
