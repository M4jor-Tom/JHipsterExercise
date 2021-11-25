import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousFamilleComponent } from './list/sous-famille.component';
import { SousFamilleDetailComponent } from './detail/sous-famille-detail.component';
import { SousFamilleUpdateComponent } from './update/sous-famille-update.component';
import { SousFamilleDeleteDialogComponent } from './delete/sous-famille-delete-dialog.component';
import { SousFamilleRoutingModule } from './route/sous-famille-routing.module';

@NgModule({
  imports: [SharedModule, SousFamilleRoutingModule],
  declarations: [SousFamilleComponent, SousFamilleDetailComponent, SousFamilleUpdateComponent, SousFamilleDeleteDialogComponent],
  entryComponents: [SousFamilleDeleteDialogComponent],
})
export class SousFamilleModule {}
