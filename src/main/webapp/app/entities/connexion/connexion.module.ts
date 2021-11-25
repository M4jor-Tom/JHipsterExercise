import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConnexionComponent } from './list/connexion.component';
import { ConnexionDetailComponent } from './detail/connexion-detail.component';
import { ConnexionUpdateComponent } from './update/connexion-update.component';
import { ConnexionDeleteDialogComponent } from './delete/connexion-delete-dialog.component';
import { ConnexionRoutingModule } from './route/connexion-routing.module';

@NgModule({
  imports: [SharedModule, ConnexionRoutingModule],
  declarations: [ConnexionComponent, ConnexionDetailComponent, ConnexionUpdateComponent, ConnexionDeleteDialogComponent],
  entryComponents: [ConnexionDeleteDialogComponent],
})
export class ConnexionModule {}
