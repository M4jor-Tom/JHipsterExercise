import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamille } from '../famille.model';
import { FamilleService } from '../service/famille.service';

@Component({
  templateUrl: './famille-delete-dialog.component.html',
})
export class FamilleDeleteDialogComponent {
  famille?: IFamille;

  constructor(protected familleService: FamilleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
