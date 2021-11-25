import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CaracteristiqueComponent } from '../list/caracteristique.component';
import { CaracteristiqueDetailComponent } from '../detail/caracteristique-detail.component';
import { CaracteristiqueUpdateComponent } from '../update/caracteristique-update.component';
import { CaracteristiqueRoutingResolveService } from './caracteristique-routing-resolve.service';

const caracteristiqueRoute: Routes = [
  {
    path: '',
    component: CaracteristiqueComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CaracteristiqueDetailComponent,
    resolve: {
      caracteristique: CaracteristiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CaracteristiqueUpdateComponent,
    resolve: {
      caracteristique: CaracteristiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CaracteristiqueUpdateComponent,
    resolve: {
      caracteristique: CaracteristiqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(caracteristiqueRoute)],
  exports: [RouterModule],
})
export class CaracteristiqueRoutingModule {}
