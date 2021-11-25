jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConnexionService } from '../service/connexion.service';
import { IConnexion, Connexion } from '../connexion.model';

import { ConnexionUpdateComponent } from './connexion-update.component';

describe('Connexion Management Update Component', () => {
  let comp: ConnexionUpdateComponent;
  let fixture: ComponentFixture<ConnexionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let connexionService: ConnexionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ConnexionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ConnexionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConnexionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    connexionService = TestBed.inject(ConnexionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const connexion: IConnexion = { id: 456 };

      activatedRoute.data = of({ connexion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(connexion));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Connexion>>();
      const connexion = { id: 123 };
      jest.spyOn(connexionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ connexion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: connexion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(connexionService.update).toHaveBeenCalledWith(connexion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Connexion>>();
      const connexion = new Connexion();
      jest.spyOn(connexionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ connexion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: connexion }));
      saveSubject.complete();

      // THEN
      expect(connexionService.create).toHaveBeenCalledWith(connexion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Connexion>>();
      const connexion = { id: 123 };
      jest.spyOn(connexionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ connexion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(connexionService.update).toHaveBeenCalledWith(connexion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
