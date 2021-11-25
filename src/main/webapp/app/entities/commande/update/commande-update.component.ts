import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;

  produitsSharedCollection: IProduit[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    iDCommande: [],
    listeProduit: [],
    somme: [],
    addresseLivraison: [],
    modePaiement: [],
    dateLivraison: [],
    produits: [],
    client: [],
  });

  constructor(
    protected commandeService: CommandeService,
    protected produitService: ProduitService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      if (commande.id === undefined) {
        const today = dayjs().startOf('day');
        commande.dateLivraison = today;
      }

      this.updateForm(commande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  trackProduitById(index: number, item: IProduit): number {
    return item.id!;
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  getSelectedProduit(option: IProduit, selectedVals?: IProduit[]): IProduit {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commande: ICommande): void {
    this.editForm.patchValue({
      id: commande.id,
      iDCommande: commande.iDCommande,
      listeProduit: commande.listeProduit,
      somme: commande.somme,
      addresseLivraison: commande.addresseLivraison,
      modePaiement: commande.modePaiement,
      dateLivraison: commande.dateLivraison ? commande.dateLivraison.format(DATE_TIME_FORMAT) : null,
      produits: commande.produits,
      client: commande.client,
    });

    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing(
      this.produitsSharedCollection,
      ...(commande.produits ?? [])
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, commande.client);
  }

  protected loadRelationshipsOptions(): void {
    this.produitService
      .query()
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) =>
          this.produitService.addProduitToCollectionIfMissing(produits, ...(this.editForm.get('produits')!.value ?? []))
        )
      )
      .subscribe((produits: IProduit[]) => (this.produitsSharedCollection = produits));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id'])!.value,
      iDCommande: this.editForm.get(['iDCommande'])!.value,
      listeProduit: this.editForm.get(['listeProduit'])!.value,
      somme: this.editForm.get(['somme'])!.value,
      addresseLivraison: this.editForm.get(['addresseLivraison'])!.value,
      modePaiement: this.editForm.get(['modePaiement'])!.value,
      dateLivraison: this.editForm.get(['dateLivraison'])!.value
        ? dayjs(this.editForm.get(['dateLivraison'])!.value, DATE_TIME_FORMAT)
        : undefined,
      produits: this.editForm.get(['produits'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
