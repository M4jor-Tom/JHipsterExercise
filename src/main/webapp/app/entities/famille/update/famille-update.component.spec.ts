jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FamilleService } from '../service/famille.service';
import { IFamille, Famille } from '../famille.model';
import { ISousFamille } from 'app/entities/sous-famille/sous-famille.model';
import { SousFamilleService } from 'app/entities/sous-famille/service/sous-famille.service';

import { FamilleUpdateComponent } from './famille-update.component';

describe('Famille Management Update Component', () => {
  let comp: FamilleUpdateComponent;
  let fixture: ComponentFixture<FamilleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familleService: FamilleService;
  let sousFamilleService: SousFamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FamilleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FamilleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familleService = TestBed.inject(FamilleService);
    sousFamilleService = TestBed.inject(SousFamilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call sousFamille query and add missing value', () => {
      const famille: IFamille = { id: 456 };
      const sousFamille: ISousFamille = { id: 16442 };
      famille.sousFamille = sousFamille;

      const sousFamilleCollection: ISousFamille[] = [{ id: 63923 }];
      jest.spyOn(sousFamilleService, 'query').mockReturnValue(of(new HttpResponse({ body: sousFamilleCollection })));
      const expectedCollection: ISousFamille[] = [sousFamille, ...sousFamilleCollection];
      jest.spyOn(sousFamilleService, 'addSousFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(sousFamilleService.query).toHaveBeenCalled();
      expect(sousFamilleService.addSousFamilleToCollectionIfMissing).toHaveBeenCalledWith(sousFamilleCollection, sousFamille);
      expect(comp.sousFamillesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const famille: IFamille = { id: 456 };
      const sousFamille: ISousFamille = { id: 73141 };
      famille.sousFamille = sousFamille;

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(famille));
      expect(comp.sousFamillesCollection).toContain(sousFamille);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = { id: 123 };
      jest.spyOn(familleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: famille }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(familleService.update).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = new Famille();
      jest.spyOn(familleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: famille }));
      saveSubject.complete();

      // THEN
      expect(familleService.create).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = { id: 123 };
      jest.spyOn(familleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familleService.update).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousFamilleById', () => {
      it('Should return tracked SousFamille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
