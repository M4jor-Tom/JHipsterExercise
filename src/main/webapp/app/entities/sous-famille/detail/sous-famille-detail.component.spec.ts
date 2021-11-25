import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousFamilleDetailComponent } from './sous-famille-detail.component';

describe('SousFamille Management Detail Component', () => {
  let comp: SousFamilleDetailComponent;
  let fixture: ComponentFixture<SousFamilleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousFamilleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousFamille: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousFamilleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousFamilleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousFamille on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousFamille).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
