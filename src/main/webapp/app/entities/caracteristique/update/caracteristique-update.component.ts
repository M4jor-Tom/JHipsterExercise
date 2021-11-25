import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICaracteristique, Caracteristique } from '../caracteristique.model';
import { CaracteristiqueService } from '../service/caracteristique.service';

@Component({
  selector: 'jhi-caracteristique-update',
  templateUrl: './caracteristique-update.component.html',
})
export class CaracteristiqueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    iDCaracteristique: [],
    marque: [],
    modele: [],
    taille: [],
    couleur: [],
  });

  constructor(
    protected caracteristiqueService: CaracteristiqueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caracteristique }) => {
      this.updateForm(caracteristique);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const caracteristique = this.createFromForm();
    if (caracteristique.id !== undefined) {
      this.subscribeToSaveResponse(this.caracteristiqueService.update(caracteristique));
    } else {
      this.subscribeToSaveResponse(this.caracteristiqueService.create(caracteristique));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaracteristique>>): void {
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

  protected updateForm(caracteristique: ICaracteristique): void {
    this.editForm.patchValue({
      id: caracteristique.id,
      iDCaracteristique: caracteristique.iDCaracteristique,
      marque: caracteristique.marque,
      modele: caracteristique.modele,
      taille: caracteristique.taille,
      couleur: caracteristique.couleur,
    });
  }

  protected createFromForm(): ICaracteristique {
    return {
      ...new Caracteristique(),
      id: this.editForm.get(['id'])!.value,
      iDCaracteristique: this.editForm.get(['iDCaracteristique'])!.value,
      marque: this.editForm.get(['marque'])!.value,
      modele: this.editForm.get(['modele'])!.value,
      taille: this.editForm.get(['taille'])!.value,
      couleur: this.editForm.get(['couleur'])!.value,
    };
  }
}
