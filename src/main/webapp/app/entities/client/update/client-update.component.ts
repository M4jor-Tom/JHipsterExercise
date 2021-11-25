import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IClient, Client } from '../client.model';
import { ClientService } from '../service/client.service';
import { IConnexion } from 'app/entities/connexion/connexion.model';
import { ConnexionService } from 'app/entities/connexion/service/connexion.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;

  connexionsCollection: IConnexion[] = [];

  editForm = this.fb.group({
    id: [],
    iDClient: [],
    dateAjout: [],
    nom: [],
    prenom: [],
    email: [],
    telephone: [],
    adresse: [],
    pays: [],
    codePostale: [],
    connexion: [],
  });

  constructor(
    protected clientService: ClientService,
    protected connexionService: ConnexionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      if (client.id === undefined) {
        const today = dayjs().startOf('day');
        client.dateAjout = today;
      }

      this.updateForm(client);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  trackConnexionById(index: number, item: IConnexion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
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

  protected updateForm(client: IClient): void {
    this.editForm.patchValue({
      id: client.id,
      iDClient: client.iDClient,
      dateAjout: client.dateAjout ? client.dateAjout.format(DATE_TIME_FORMAT) : null,
      nom: client.nom,
      prenom: client.prenom,
      email: client.email,
      telephone: client.telephone,
      adresse: client.adresse,
      pays: client.pays,
      codePostale: client.codePostale,
      connexion: client.connexion,
    });

    this.connexionsCollection = this.connexionService.addConnexionToCollectionIfMissing(this.connexionsCollection, client.connexion);
  }

  protected loadRelationshipsOptions(): void {
    this.connexionService
      .query({ filter: 'client-is-null' })
      .pipe(map((res: HttpResponse<IConnexion[]>) => res.body ?? []))
      .pipe(
        map((connexions: IConnexion[]) =>
          this.connexionService.addConnexionToCollectionIfMissing(connexions, this.editForm.get('connexion')!.value)
        )
      )
      .subscribe((connexions: IConnexion[]) => (this.connexionsCollection = connexions));
  }

  protected createFromForm(): IClient {
    return {
      ...new Client(),
      id: this.editForm.get(['id'])!.value,
      iDClient: this.editForm.get(['iDClient'])!.value,
      dateAjout: this.editForm.get(['dateAjout'])!.value ? dayjs(this.editForm.get(['dateAjout'])!.value, DATE_TIME_FORMAT) : undefined,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      pays: this.editForm.get(['pays'])!.value,
      codePostale: this.editForm.get(['codePostale'])!.value,
      connexion: this.editForm.get(['connexion'])!.value,
    };
  }
}
