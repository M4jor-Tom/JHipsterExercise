jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConnexion, Connexion } from '../connexion.model';
import { ConnexionService } from '../service/connexion.service';

import { ConnexionRoutingResolveService } from './connexion-routing-resolve.service';

describe('Connexion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ConnexionRoutingResolveService;
  let service: ConnexionService;
  let resultConnexion: IConnexion | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ConnexionRoutingResolveService);
    service = TestBed.inject(ConnexionService);
    resultConnexion = undefined;
  });

  describe('resolve', () => {
    it('should return IConnexion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConnexion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultConnexion).toEqual({ id: 123 });
    });

    it('should return new IConnexion if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConnexion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultConnexion).toEqual(new Connexion());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Connexion })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConnexion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultConnexion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
