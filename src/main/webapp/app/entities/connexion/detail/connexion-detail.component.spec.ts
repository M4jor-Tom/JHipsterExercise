import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConnexionDetailComponent } from './connexion-detail.component';

describe('Connexion Management Detail Component', () => {
  let comp: ConnexionDetailComponent;
  let fixture: ComponentFixture<ConnexionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConnexionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ connexion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConnexionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConnexionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load connexion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.connexion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
