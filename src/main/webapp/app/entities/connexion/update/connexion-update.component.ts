import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IConnexion, Connexion } from '../connexion.model';
import { ConnexionService } from '../service/connexion.service';

@Component({
  selector: 'jhi-connexion-update',
  templateUrl: './connexion-update.component.html',
})
export class ConnexionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    iDConnexion: [],
    username: [],
    password: [],
  });

  constructor(protected connexionService: ConnexionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ connexion }) => {
      this.updateForm(connexion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const connexion = this.createFromForm();
    if (connexion.id !== undefined) {
      this.subscribeToSaveResponse(this.connexionService.update(connexion));
    } else {
      this.subscribeToSaveResponse(this.connexionService.create(connexion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConnexion>>): void {
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

  protected updateForm(connexion: IConnexion): void {
    this.editForm.patchValue({
      id: connexion.id,
      iDConnexion: connexion.iDConnexion,
      username: connexion.username,
      password: connexion.password,
    });
  }

  protected createFromForm(): IConnexion {
    return {
      ...new Connexion(),
      id: this.editForm.get(['id'])!.value,
      iDConnexion: this.editForm.get(['iDConnexion'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
    };
  }
}
