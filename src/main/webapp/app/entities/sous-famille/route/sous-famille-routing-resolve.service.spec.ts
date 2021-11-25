jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousFamille, SousFamille } from '../sous-famille.model';
import { SousFamilleService } from '../service/sous-famille.service';

import { SousFamilleRoutingResolveService } from './sous-famille-routing-resolve.service';

describe('SousFamille routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousFamilleRoutingResolveService;
  let service: SousFamilleService;
  let resultSousFamille: ISousFamille | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousFamilleRoutingResolveService);
    service = TestBed.inject(SousFamilleService);
    resultSousFamille = undefined;
  });

  describe('resolve', () => {
    it('should return ISousFamille returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousFamille = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousFamille).toEqual({ id: 123 });
    });

    it('should return new ISousFamille if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousFamille = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousFamille).toEqual(new SousFamille());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousFamille })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousFamille = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousFamille).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
