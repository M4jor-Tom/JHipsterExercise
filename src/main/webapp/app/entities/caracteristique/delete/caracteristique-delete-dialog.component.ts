import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaracteristique } from '../caracteristique.model';
import { CaracteristiqueService } from '../service/caracteristique.service';

@Component({
  templateUrl: './caracteristique-delete-dialog.component.html',
})
export class CaracteristiqueDeleteDialogComponent {
  caracteristique?: ICaracteristique;

  constructor(protected caracteristiqueService: CaracteristiqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.caracteristiqueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
