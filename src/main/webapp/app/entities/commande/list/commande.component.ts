import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { CommandeDeleteDialogComponent } from '../delete/commande-delete-dialog.component';

@Component({
  selector: 'jhi-commande',
  templateUrl: './commande.component.html',
})
export class CommandeComponent implements OnInit {
  commandes?: ICommande[];
  isLoading = false;

  constructor(protected commandeService: CommandeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.commandeService.query().subscribe(
      (res: HttpResponse<ICommande[]>) => {
        this.isLoading = false;
        this.commandes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICommande): number {
    return item.id!;
  }

  delete(commande: ICommande): void {
    const modalRef = this.modalService.open(CommandeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.commande = commande;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
