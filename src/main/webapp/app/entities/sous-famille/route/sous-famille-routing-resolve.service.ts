import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousFamille, SousFamille } from '../sous-famille.model';
import { SousFamilleService } from '../service/sous-famille.service';

@Injectable({ providedIn: 'root' })
export class SousFamilleRoutingResolveService implements Resolve<ISousFamille> {
  constructor(protected service: SousFamilleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousFamille> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousFamille: HttpResponse<SousFamille>) => {
          if (sousFamille.body) {
            return of(sousFamille.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousFamille());
  }
}
