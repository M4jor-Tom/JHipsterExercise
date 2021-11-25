import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICaracteristique, getCaracteristiqueIdentifier } from '../caracteristique.model';

export type EntityResponseType = HttpResponse<ICaracteristique>;
export type EntityArrayResponseType = HttpResponse<ICaracteristique[]>;

@Injectable({ providedIn: 'root' })
export class CaracteristiqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/caracteristiques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(caracteristique: ICaracteristique): Observable<EntityResponseType> {
    return this.http.post<ICaracteristique>(this.resourceUrl, caracteristique, { observe: 'response' });
  }

  update(caracteristique: ICaracteristique): Observable<EntityResponseType> {
    return this.http.put<ICaracteristique>(
      `${this.resourceUrl}/${getCaracteristiqueIdentifier(caracteristique) as number}`,
      caracteristique,
      { observe: 'response' }
    );
  }

  partialUpdate(caracteristique: ICaracteristique): Observable<EntityResponseType> {
    return this.http.patch<ICaracteristique>(
      `${this.resourceUrl}/${getCaracteristiqueIdentifier(caracteristique) as number}`,
      caracteristique,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICaracteristique>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICaracteristique[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCaracteristiqueToCollectionIfMissing(
    caracteristiqueCollection: ICaracteristique[],
    ...caracteristiquesToCheck: (ICaracteristique | null | undefined)[]
  ): ICaracteristique[] {
    const caracteristiques: ICaracteristique[] = caracteristiquesToCheck.filter(isPresent);
    if (caracteristiques.length > 0) {
      const caracteristiqueCollectionIdentifiers = caracteristiqueCollection.map(
        caracteristiqueItem => getCaracteristiqueIdentifier(caracteristiqueItem)!
      );
      const caracteristiquesToAdd = caracteristiques.filter(caracteristiqueItem => {
        const caracteristiqueIdentifier = getCaracteristiqueIdentifier(caracteristiqueItem);
        if (caracteristiqueIdentifier == null || caracteristiqueCollectionIdentifiers.includes(caracteristiqueIdentifier)) {
          return false;
        }
        caracteristiqueCollectionIdentifiers.push(caracteristiqueIdentifier);
        return true;
      });
      return [...caracteristiquesToAdd, ...caracteristiqueCollection];
    }
    return caracteristiqueCollection;
  }
}
