import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConnexionComponent } from '../list/connexion.component';
import { ConnexionDetailComponent } from '../detail/connexion-detail.component';
import { ConnexionUpdateComponent } from '../update/connexion-update.component';
import { ConnexionRoutingResolveService } from './connexion-routing-resolve.service';

const connexionRoute: Routes = [
  {
    path: '',
    component: ConnexionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConnexionDetailComponent,
    resolve: {
      connexion: ConnexionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConnexionUpdateComponent,
    resolve: {
      connexion: ConnexionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConnexionUpdateComponent,
    resolve: {
      connexion: ConnexionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(connexionRoute)],
  exports: [RouterModule],
})
export class ConnexionRoutingModule {}
