import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICaracteristique, Caracteristique } from '../caracteristique.model';
import { CaracteristiqueService } from '../service/caracteristique.service';

@Injectable({ providedIn: 'root' })
export class CaracteristiqueRoutingResolveService implements Resolve<ICaracteristique> {
  constructor(protected service: CaracteristiqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICaracteristique> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((caracteristique: HttpResponse<Caracteristique>) => {
          if (caracteristique.body) {
            return of(caracteristique.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Caracteristique());
  }
}
