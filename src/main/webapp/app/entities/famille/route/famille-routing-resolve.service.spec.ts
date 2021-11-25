jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFamille, Famille } from '../famille.model';
import { FamilleService } from '../service/famille.service';

import { FamilleRoutingResolveService } from './famille-routing-resolve.service';

describe('Famille routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FamilleRoutingResolveService;
  let service: FamilleService;
  let resultFamille: IFamille | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FamilleRoutingResolveService);
    service = TestBed.inject(FamilleService);
    resultFamille = undefined;
  });

  describe('resolve', () => {
    it('should return IFamille returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFamille = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFamille).toEqual({ id: 123 });
    });

    it('should return new IFamille if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFamille = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFamille).toEqual(new Famille());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Famille })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFamille = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFamille).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
