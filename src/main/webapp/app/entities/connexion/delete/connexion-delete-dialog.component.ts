import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConnexion } from '../connexion.model';
import { ConnexionService } from '../service/connexion.service';

@Component({
  templateUrl: './connexion-delete-dialog.component.html',
})
export class ConnexionDeleteDialogComponent {
  connexion?: IConnexion;

  constructor(protected connexionService: ConnexionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.connexionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
