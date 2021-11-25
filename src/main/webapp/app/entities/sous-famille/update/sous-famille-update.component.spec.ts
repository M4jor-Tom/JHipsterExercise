jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousFamilleService } from '../service/sous-famille.service';
import { ISousFamille, SousFamille } from '../sous-famille.model';

import { SousFamilleUpdateComponent } from './sous-famille-update.component';

describe('SousFamille Management Update Component', () => {
  let comp: SousFamilleUpdateComponent;
  let fixture: ComponentFixture<SousFamilleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousFamilleService: SousFamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousFamilleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousFamilleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousFamilleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousFamilleService = TestBed.inject(SousFamilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sousFamille: ISousFamille = { id: 456 };

      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousFamille));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousFamille>>();
      const sousFamille = { id: 123 };
      jest.spyOn(sousFamilleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousFamille }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousFamilleService.update).toHaveBeenCalledWith(sousFamille);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousFamille>>();
      const sousFamille = new SousFamille();
      jest.spyOn(sousFamilleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousFamille }));
      saveSubject.complete();

      // THEN
      expect(sousFamilleService.create).toHaveBeenCalledWith(sousFamille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousFamille>>();
      const sousFamille = { id: 123 };
      jest.spyOn(sousFamilleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousFamilleService.update).toHaveBeenCalledWith(sousFamille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
