import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConnexion } from '../connexion.model';

@Component({
  selector: 'jhi-connexion-detail',
  templateUrl: './connexion-detail.component.html',
})
export class ConnexionDetailComponent implements OnInit {
  connexion: IConnexion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ connexion }) => {
      this.connexion = connexion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
