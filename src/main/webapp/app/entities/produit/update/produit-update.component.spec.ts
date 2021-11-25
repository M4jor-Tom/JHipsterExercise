jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProduitService } from '../service/produit.service';
import { IProduit, Produit } from '../produit.model';
import { ICaracteristique } from 'app/entities/caracteristique/caracteristique.model';
import { CaracteristiqueService } from 'app/entities/caracteristique/service/caracteristique.service';
import { IFamille } from 'app/entities/famille/famille.model';
import { FamilleService } from 'app/entities/famille/service/famille.service';

import { ProduitUpdateComponent } from './produit-update.component';

describe('Produit Management Update Component', () => {
  let comp: ProduitUpdateComponent;
  let fixture: ComponentFixture<ProduitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let produitService: ProduitService;
  let caracteristiqueService: CaracteristiqueService;
  let familleService: FamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProduitUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ProduitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProduitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    produitService = TestBed.inject(ProduitService);
    caracteristiqueService = TestBed.inject(CaracteristiqueService);
    familleService = TestBed.inject(FamilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call caracteristique query and add missing value', () => {
      const produit: IProduit = { id: 456 };
      const caracteristique: ICaracteristique = { id: 57442 };
      produit.caracteristique = caracteristique;

      const caracteristiqueCollection: ICaracteristique[] = [{ id: 44294 }];
      jest.spyOn(caracteristiqueService, 'query').mockReturnValue(of(new HttpResponse({ body: caracteristiqueCollection })));
      const expectedCollection: ICaracteristique[] = [caracteristique, ...caracteristiqueCollection];
      jest.spyOn(caracteristiqueService, 'addCaracteristiqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(caracteristiqueService.query).toHaveBeenCalled();
      expect(caracteristiqueService.addCaracteristiqueToCollectionIfMissing).toHaveBeenCalledWith(
        caracteristiqueCollection,
        caracteristique
      );
      expect(comp.caracteristiquesCollection).toEqual(expectedCollection);
    });

    it('Should call famille query and add missing value', () => {
      const produit: IProduit = { id: 456 };
      const famille: IFamille = { id: 63005 };
      produit.famille = famille;

      const familleCollection: IFamille[] = [{ id: 9364 }];
      jest.spyOn(familleService, 'query').mockReturnValue(of(new HttpResponse({ body: familleCollection })));
      const expectedCollection: IFamille[] = [famille, ...familleCollection];
      jest.spyOn(familleService, 'addFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(familleService.query).toHaveBeenCalled();
      expect(familleService.addFamilleToCollectionIfMissing).toHaveBeenCalledWith(familleCollection, famille);
      expect(comp.famillesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const produit: IProduit = { id: 456 };
      const caracteristique: ICaracteristique = { id: 24274 };
      produit.caracteristique = caracteristique;
      const famille: IFamille = { id: 18231 };
      produit.famille = famille;

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(produit));
      expect(comp.caracteristiquesCollection).toContain(caracteristique);
      expect(comp.famillesCollection).toContain(famille);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produit>>();
      const produit = { id: 123 };
      jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(produitService.update).toHaveBeenCalledWith(produit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produit>>();
      const produit = new Produit();
      jest.spyOn(produitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produit }));
      saveSubject.complete();

      // THEN
      expect(produitService.create).toHaveBeenCalledWith(produit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produit>>();
      const produit = { id: 123 };
      jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(produitService.update).toHaveBeenCalledWith(produit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCaracteristiqueById', () => {
      it('Should return tracked Caracteristique primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCaracteristiqueById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFamilleById', () => {
      it('Should return tracked Famille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
