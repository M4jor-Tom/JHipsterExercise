import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CaracteristiqueDetailComponent } from './caracteristique-detail.component';

describe('Caracteristique Management Detail Component', () => {
  let comp: CaracteristiqueDetailComponent;
  let fixture: ComponentFixture<CaracteristiqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CaracteristiqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ caracteristique: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CaracteristiqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CaracteristiqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load caracteristique on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.caracteristique).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
