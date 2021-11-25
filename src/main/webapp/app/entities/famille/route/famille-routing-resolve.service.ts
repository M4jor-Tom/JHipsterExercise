import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFamille, Famille } from '../famille.model';
import { FamilleService } from '../service/famille.service';

@Injectable({ providedIn: 'root' })
export class FamilleRoutingResolveService implements Resolve<IFamille> {
  constructor(protected service: FamilleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFamille> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((famille: HttpResponse<Famille>) => {
          if (famille.body) {
            return of(famille.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Famille());
  }
}
