import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousFamilleComponent } from '../list/sous-famille.component';
import { SousFamilleDetailComponent } from '../detail/sous-famille-detail.component';
import { SousFamilleUpdateComponent } from '../update/sous-famille-update.component';
import { SousFamilleRoutingResolveService } from './sous-famille-routing-resolve.service';

const sousFamilleRoute: Routes = [
  {
    path: '',
    component: SousFamilleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousFamilleDetailComponent,
    resolve: {
      sousFamille: SousFamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousFamilleUpdateComponent,
    resolve: {
      sousFamille: SousFamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousFamilleUpdateComponent,
    resolve: {
      sousFamille: SousFamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousFamilleRoute)],
  exports: [RouterModule],
})
export class SousFamilleRoutingModule {}
