import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CaracteristiqueComponent } from './list/caracteristique.component';
import { CaracteristiqueDetailComponent } from './detail/caracteristique-detail.component';
import { CaracteristiqueUpdateComponent } from './update/caracteristique-update.component';
import { CaracteristiqueDeleteDialogComponent } from './delete/caracteristique-delete-dialog.component';
import { CaracteristiqueRoutingModule } from './route/caracteristique-routing.module';

@NgModule({
  imports: [SharedModule, CaracteristiqueRoutingModule],
  declarations: [
    CaracteristiqueComponent,
    CaracteristiqueDetailComponent,
    CaracteristiqueUpdateComponent,
    CaracteristiqueDeleteDialogComponent,
  ],
  entryComponents: [CaracteristiqueDeleteDialogComponent],
})
export class CaracteristiqueModule {}
