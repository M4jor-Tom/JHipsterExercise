import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFamille, Famille } from '../famille.model';
import { FamilleService } from '../service/famille.service';
import { ISousFamille } from 'app/entities/sous-famille/sous-famille.model';
import { SousFamilleService } from 'app/entities/sous-famille/service/sous-famille.service';

@Component({
  selector: 'jhi-famille-update',
  templateUrl: './famille-update.component.html',
})
export class FamilleUpdateComponent implements OnInit {
  isSaving = false;

  sousFamillesCollection: ISousFamille[] = [];

  editForm = this.fb.group({
    id: [],
    iDFamille: [],
    famille: [],
    sousFamille: [],
  });

  constructor(
    protected familleService: FamilleService,
    protected sousFamilleService: SousFamilleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ famille }) => {
      this.updateForm(famille);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const famille = this.createFromForm();
    if (famille.id !== undefined) {
      this.subscribeToSaveResponse(this.familleService.update(famille));
    } else {
      this.subscribeToSaveResponse(this.familleService.create(famille));
    }
  }

  trackSousFamilleById(index: number, item: ISousFamille): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamille>>): void {
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

  protected updateForm(famille: IFamille): void {
    this.editForm.patchValue({
      id: famille.id,
      iDFamille: famille.iDFamille,
      famille: famille.famille,
      sousFamille: famille.sousFamille,
    });

    this.sousFamillesCollection = this.sousFamilleService.addSousFamilleToCollectionIfMissing(
      this.sousFamillesCollection,
      famille.sousFamille
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sousFamilleService
      .query({ filter: 'famille-is-null' })
      .pipe(map((res: HttpResponse<ISousFamille[]>) => res.body ?? []))
      .pipe(
        map((sousFamilles: ISousFamille[]) =>
          this.sousFamilleService.addSousFamilleToCollectionIfMissing(sousFamilles, this.editForm.get('sousFamille')!.value)
        )
      )
      .subscribe((sousFamilles: ISousFamille[]) => (this.sousFamillesCollection = sousFamilles));
  }

  protected createFromForm(): IFamille {
    return {
      ...new Famille(),
      id: this.editForm.get(['id'])!.value,
      iDFamille: this.editForm.get(['iDFamille'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      sousFamille: this.editForm.get(['sousFamille'])!.value,
    };
  }
}
