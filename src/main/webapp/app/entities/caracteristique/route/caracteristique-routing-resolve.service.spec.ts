jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICaracteristique, Caracteristique } from '../caracteristique.model';
import { CaracteristiqueService } from '../service/caracteristique.service';

import { CaracteristiqueRoutingResolveService } from './caracteristique-routing-resolve.service';

describe('Caracteristique routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CaracteristiqueRoutingResolveService;
  let service: CaracteristiqueService;
  let resultCaracteristique: ICaracteristique | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CaracteristiqueRoutingResolveService);
    service = TestBed.inject(CaracteristiqueService);
    resultCaracteristique = undefined;
  });

  describe('resolve', () => {
    it('should return ICaracteristique returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCaracteristique = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCaracteristique).toEqual({ id: 123 });
    });

    it('should return new ICaracteristique if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCaracteristique = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCaracteristique).toEqual(new Caracteristique());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Caracteristique })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCaracteristique = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCaracteristique).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
