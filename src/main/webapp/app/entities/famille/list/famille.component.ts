import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamille } from '../famille.model';
import { FamilleService } from '../service/famille.service';
import { FamilleDeleteDialogComponent } from '../delete/famille-delete-dialog.component';

@Component({
  selector: 'jhi-famille',
  templateUrl: './famille.component.html',
})
export class FamilleComponent implements OnInit {
  familles?: IFamille[];
  isLoading = false;

  constructor(protected familleService: FamilleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.familleService.query().subscribe(
      (res: HttpResponse<IFamille[]>) => {
        this.isLoading = false;
        this.familles = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFamille): number {
    return item.id!;
  }

  delete(famille: IFamille): void {
    const modalRef = this.modalService.open(FamilleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.famille = famille;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
