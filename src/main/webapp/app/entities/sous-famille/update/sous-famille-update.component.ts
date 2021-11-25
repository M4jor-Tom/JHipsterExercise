import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISousFamille, SousFamille } from '../sous-famille.model';
import { SousFamilleService } from '../service/sous-famille.service';

@Component({
  selector: 'jhi-sous-famille-update',
  templateUrl: './sous-famille-update.component.html',
})
export class SousFamilleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    iDSousFamille: [],
    sousFamille: [],
  });

  constructor(protected sousFamilleService: SousFamilleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousFamille }) => {
      this.updateForm(sousFamille);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousFamille = this.createFromForm();
    if (sousFamille.id !== undefined) {
      this.subscribeToSaveResponse(this.sousFamilleService.update(sousFamille));
    } else {
      this.subscribeToSaveResponse(this.sousFamilleService.create(sousFamille));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousFamille>>): void {
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

  protected updateForm(sousFamille: ISousFamille): void {
    this.editForm.patchValue({
      id: sousFamille.id,
      iDSousFamille: sousFamille.iDSousFamille,
      sousFamille: sousFamille.sousFamille,
    });
  }

  protected createFromForm(): ISousFamille {
    return {
      ...new SousFamille(),
      id: this.editForm.get(['id'])!.value,
      iDSousFamille: this.editForm.get(['iDSousFamille'])!.value,
      sousFamille: this.editForm.get(['sousFamille'])!.value,
    };
  }
}
