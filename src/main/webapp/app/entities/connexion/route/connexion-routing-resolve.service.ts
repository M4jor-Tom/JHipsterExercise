import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConnexion, Connexion } from '../connexion.model';
import { ConnexionService } from '../service/connexion.service';

@Injectable({ providedIn: 'root' })
export class ConnexionRoutingResolveService implements Resolve<IConnexion> {
  constructor(protected service: ConnexionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConnexion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((connexion: HttpResponse<Connexion>) => {
          if (connexion.body) {
            return of(connexion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Connexion());
  }
}
