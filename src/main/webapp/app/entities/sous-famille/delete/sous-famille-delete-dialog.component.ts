import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousFamille } from '../sous-famille.model';
import { SousFamilleService } from '../service/sous-famille.service';

@Component({
  templateUrl: './sous-famille-delete-dialog.component.html',
})
export class SousFamilleDeleteDialogComponent {
  sousFamille?: ISousFamille;

  constructor(protected sousFamilleService: SousFamilleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousFamilleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
