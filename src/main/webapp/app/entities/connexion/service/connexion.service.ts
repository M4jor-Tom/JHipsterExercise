import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConnexion, getConnexionIdentifier } from '../connexion.model';

export type EntityResponseType = HttpResponse<IConnexion>;
export type EntityArrayResponseType = HttpResponse<IConnexion[]>;

@Injectable({ providedIn: 'root' })
export class ConnexionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/connexions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(connexion: IConnexion): Observable<EntityResponseType> {
    return this.http.post<IConnexion>(this.resourceUrl, connexion, { observe: 'response' });
  }

  update(connexion: IConnexion): Observable<EntityResponseType> {
    return this.http.put<IConnexion>(`${this.resourceUrl}/${getConnexionIdentifier(connexion) as number}`, connexion, {
      observe: 'response',
    });
  }

  partialUpdate(connexion: IConnexion): Observable<EntityResponseType> {
    return this.http.patch<IConnexion>(`${this.resourceUrl}/${getConnexionIdentifier(connexion) as number}`, connexion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConnexion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConnexion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConnexionToCollectionIfMissing(
    connexionCollection: IConnexion[],
    ...connexionsToCheck: (IConnexion | null | undefined)[]
  ): IConnexion[] {
    const connexions: IConnexion[] = connexionsToCheck.filter(isPresent);
    if (connexions.length > 0) {
      const connexionCollectionIdentifiers = connexionCollection.map(connexionItem => getConnexionIdentifier(connexionItem)!);
      const connexionsToAdd = connexions.filter(connexionItem => {
        const connexionIdentifier = getConnexionIdentifier(connexionItem);
        if (connexionIdentifier == null || connexionCollectionIdentifiers.includes(connexionIdentifier)) {
          return false;
        }
        connexionCollectionIdentifiers.push(connexionIdentifier);
        return true;
      });
      return [...connexionsToAdd, ...connexionCollection];
    }
    return connexionCollection;
  }
}
