import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaracteristique } from '../caracteristique.model';
import { CaracteristiqueService } from '../service/caracteristique.service';
import { CaracteristiqueDeleteDialogComponent } from '../delete/caracteristique-delete-dialog.component';

@Component({
  selector: 'jhi-caracteristique',
  templateUrl: './caracteristique.component.html',
})
export class CaracteristiqueComponent implements OnInit {
  caracteristiques?: ICaracteristique[];
  isLoading = false;

  constructor(protected caracteristiqueService: CaracteristiqueService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.caracteristiqueService.query().subscribe(
      (res: HttpResponse<ICaracteristique[]>) => {
        this.isLoading = false;
        this.caracteristiques = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICaracteristique): number {
    return item.id!;
  }

  delete(caracteristique: ICaracteristique): void {
    const modalRef = this.modalService.open(CaracteristiqueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.caracteristique = caracteristique;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
