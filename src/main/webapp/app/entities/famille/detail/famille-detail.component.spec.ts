import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilleDetailComponent } from './famille-detail.component';

describe('Famille Management Detail Component', () => {
  let comp: FamilleDetailComponent;
  let fixture: ComponentFixture<FamilleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FamilleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ famille: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FamilleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FamilleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load famille on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.famille).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
