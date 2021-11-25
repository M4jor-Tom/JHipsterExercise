import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICaracteristique } from '../caracteristique.model';

@Component({
  selector: 'jhi-caracteristique-detail',
  templateUrl: './caracteristique-detail.component.html',
})
export class CaracteristiqueDetailComponent implements OnInit {
  caracteristique: ICaracteristique | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caracteristique }) => {
      this.caracteristique = caracteristique;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
