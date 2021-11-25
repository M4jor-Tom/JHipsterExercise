jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CaracteristiqueService } from '../service/caracteristique.service';
import { ICaracteristique, Caracteristique } from '../caracteristique.model';

import { CaracteristiqueUpdateComponent } from './caracteristique-update.component';

describe('Caracteristique Management Update Component', () => {
  let comp: CaracteristiqueUpdateComponent;
  let fixture: ComponentFixture<CaracteristiqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let caracteristiqueService: CaracteristiqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CaracteristiqueUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CaracteristiqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CaracteristiqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    caracteristiqueService = TestBed.inject(CaracteristiqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const caracteristique: ICaracteristique = { id: 456 };

      activatedRoute.data = of({ caracteristique });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(caracteristique));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caracteristique>>();
      const caracteristique = { id: 123 };
      jest.spyOn(caracteristiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caracteristique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caracteristique }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(caracteristiqueService.update).toHaveBeenCalledWith(caracteristique);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caracteristique>>();
      const caracteristique = new Caracteristique();
      jest.spyOn(caracteristiqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caracteristique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caracteristique }));
      saveSubject.complete();

      // THEN
      expect(caracteristiqueService.create).toHaveBeenCalledWith(caracteristique);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caracteristique>>();
      const caracteristique = { id: 123 };
      jest.spyOn(caracteristiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caracteristique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(caracteristiqueService.update).toHaveBeenCalledWith(caracteristique);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
