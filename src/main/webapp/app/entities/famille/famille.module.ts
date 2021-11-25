import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FamilleComponent } from './list/famille.component';
import { FamilleDetailComponent } from './detail/famille-detail.component';
import { FamilleUpdateComponent } from './update/famille-update.component';
import { FamilleDeleteDialogComponent } from './delete/famille-delete-dialog.component';
import { FamilleRoutingModule } from './route/famille-routing.module';

@NgModule({
  imports: [SharedModule, FamilleRoutingModule],
  declarations: [FamilleComponent, FamilleDetailComponent, FamilleUpdateComponent, FamilleDeleteDialogComponent],
  entryComponents: [FamilleDeleteDialogComponent],
})
export class FamilleModule {}
