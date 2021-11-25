import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousFamille } from '../sous-famille.model';

@Component({
  selector: 'jhi-sous-famille-detail',
  templateUrl: './sous-famille-detail.component.html',
})
export class SousFamilleDetailComponent implements OnInit {
  sousFamille: ISousFamille | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousFamille }) => {
      this.sousFamille = sousFamille;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
