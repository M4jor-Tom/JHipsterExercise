import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduit, Produit } from '../produit.model';
import { ProduitService } from '../service/produit.service';
import { ICaracteristique } from 'app/entities/caracteristique/caracteristique.model';
import { CaracteristiqueService } from 'app/entities/caracteristique/service/caracteristique.service';
import { IFamille } from 'app/entities/famille/famille.model';
import { FamilleService } from 'app/entities/famille/service/famille.service';

@Component({
  selector: 'jhi-produit-update',
  templateUrl: './produit-update.component.html',
})
export class ProduitUpdateComponent implements OnInit {
  isSaving = false;

  caracteristiquesCollection: ICaracteristique[] = [];
  famillesCollection: IFamille[] = [];

  editForm = this.fb.group({
    id: [],
    iDProduit: [],
    nom: [],
    description: [],
    photo: [],
    stock: [],
    tags: [],
    tarif: [],
    caracteristique: [],
    famille: [],
  });

  constructor(
    protected produitService: ProduitService,
    protected caracteristiqueService: CaracteristiqueService,
    protected familleService: FamilleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produit }) => {
      this.updateForm(produit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produit = this.createFromForm();
    if (produit.id !== undefined) {
      this.subscribeToSaveResponse(this.produitService.update(produit));
    } else {
      this.subscribeToSaveResponse(this.produitService.create(produit));
    }
  }

  trackCaracteristiqueById(index: number, item: ICaracteristique): number {
    return item.id!;
  }

  trackFamilleById(index: number, item: IFamille): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduit>>): void {
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

  protected updateForm(produit: IProduit): void {
    this.editForm.patchValue({
      id: produit.id,
      iDProduit: produit.iDProduit,
      nom: produit.nom,
      description: produit.description,
      photo: produit.photo,
      stock: produit.stock,
      tags: produit.tags,
      tarif: produit.tarif,
      caracteristique: produit.caracteristique,
      famille: produit.famille,
    });

    this.caracteristiquesCollection = this.caracteristiqueService.addCaracteristiqueToCollectionIfMissing(
      this.caracteristiquesCollection,
      produit.caracteristique
    );
    this.famillesCollection = this.familleService.addFamilleToCollectionIfMissing(this.famillesCollection, produit.famille);
  }

  protected loadRelationshipsOptions(): void {
    this.caracteristiqueService
      .query({ filter: 'produit-is-null' })
      .pipe(map((res: HttpResponse<ICaracteristique[]>) => res.body ?? []))
      .pipe(
        map((caracteristiques: ICaracteristique[]) =>
          this.caracteristiqueService.addCaracteristiqueToCollectionIfMissing(caracteristiques, this.editForm.get('caracteristique')!.value)
        )
      )
      .subscribe((caracteristiques: ICaracteristique[]) => (this.caracteristiquesCollection = caracteristiques));

    this.familleService
      .query({ filter: 'produit-is-null' })
      .pipe(map((res: HttpResponse<IFamille[]>) => res.body ?? []))
      .pipe(
        map((familles: IFamille[]) => this.familleService.addFamilleToCollectionIfMissing(familles, this.editForm.get('famille')!.value))
      )
      .subscribe((familles: IFamille[]) => (this.famillesCollection = familles));
  }

  protected createFromForm(): IProduit {
    return {
      ...new Produit(),
      id: this.editForm.get(['id'])!.value,
      iDProduit: this.editForm.get(['iDProduit'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      tarif: this.editForm.get(['tarif'])!.value,
      caracteristique: this.editForm.get(['caracteristique'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
