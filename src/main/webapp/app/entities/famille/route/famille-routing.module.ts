import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FamilleComponent } from '../list/famille.component';
import { FamilleDetailComponent } from '../detail/famille-detail.component';
import { FamilleUpdateComponent } from '../update/famille-update.component';
import { FamilleRoutingResolveService } from './famille-routing-resolve.service';

const familleRoute: Routes = [
  {
    path: '',
    component: FamilleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FamilleDetailComponent,
    resolve: {
      famille: FamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FamilleUpdateComponent,
    resolve: {
      famille: FamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FamilleUpdateComponent,
    resolve: {
      famille: FamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(familleRoute)],
  exports: [RouterModule],
})
export class FamilleRoutingModule {}
