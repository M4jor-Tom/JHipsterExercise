import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousFamille } from '../sous-famille.model';
import { SousFamilleService } from '../service/sous-famille.service';
import { SousFamilleDeleteDialogComponent } from '../delete/sous-famille-delete-dialog.component';

@Component({
  selector: 'jhi-sous-famille',
  templateUrl: './sous-famille.component.html',
})
export class SousFamilleComponent implements OnInit {
  sousFamilles?: ISousFamille[];
  isLoading = false;

  constructor(protected sousFamilleService: SousFamilleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sousFamilleService.query().subscribe(
      (res: HttpResponse<ISousFamille[]>) => {
        this.isLoading = false;
        this.sousFamilles = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISousFamille): number {
    return item.id!;
  }

  delete(sousFamille: ISousFamille): void {
    const modalRef = this.modalService.open(SousFamilleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sousFamille = sousFamille;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
