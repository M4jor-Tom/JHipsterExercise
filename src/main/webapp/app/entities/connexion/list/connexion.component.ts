import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConnexion } from '../connexion.model';
import { ConnexionService } from '../service/connexion.service';
import { ConnexionDeleteDialogComponent } from '../delete/connexion-delete-dialog.component';

@Component({
  selector: 'jhi-connexion',
  templateUrl: './connexion.component.html',
})
export class ConnexionComponent implements OnInit {
  connexions?: IConnexion[];
  isLoading = false;

  constructor(protected connexionService: ConnexionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.connexionService.query().subscribe(
      (res: HttpResponse<IConnexion[]>) => {
        this.isLoading = false;
        this.connexions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IConnexion): number {
    return item.id!;
  }

  delete(connexion: IConnexion): void {
    const modalRef = this.modalService.open(ConnexionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.connexion = connexion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
