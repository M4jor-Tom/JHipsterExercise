import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousFamille, SousFamille } from '../sous-famille.model';

import { SousFamilleService } from './sous-famille.service';

describe('SousFamille Service', () => {
  let service: SousFamilleService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousFamille;
  let expectedResult: ISousFamille | ISousFamille[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousFamilleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      iDSousFamille: 0,
      sousFamille: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SousFamille', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousFamille()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousFamille', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDSousFamille: 1,
          sousFamille: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SousFamille', () => {
      const patchObject = Object.assign(
        {
          sousFamille: 'BBBBBB',
        },
        new SousFamille()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousFamille', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDSousFamille: 1,
          sousFamille: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SousFamille', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousFamilleToCollectionIfMissing', () => {
      it('should add a SousFamille to an empty array', () => {
        const sousFamille: ISousFamille = { id: 123 };
        expectedResult = service.addSousFamilleToCollectionIfMissing([], sousFamille);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousFamille);
      });

      it('should not add a SousFamille to an array that contains it', () => {
        const sousFamille: ISousFamille = { id: 123 };
        const sousFamilleCollection: ISousFamille[] = [
          {
            ...sousFamille,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousFamilleToCollectionIfMissing(sousFamilleCollection, sousFamille);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousFamille to an array that doesn't contain it", () => {
        const sousFamille: ISousFamille = { id: 123 };
        const sousFamilleCollection: ISousFamille[] = [{ id: 456 }];
        expectedResult = service.addSousFamilleToCollectionIfMissing(sousFamilleCollection, sousFamille);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousFamille);
      });

      it('should add only unique SousFamille to an array', () => {
        const sousFamilleArray: ISousFamille[] = [{ id: 123 }, { id: 456 }, { id: 69277 }];
        const sousFamilleCollection: ISousFamille[] = [{ id: 123 }];
        expectedResult = service.addSousFamilleToCollectionIfMissing(sousFamilleCollection, ...sousFamilleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousFamille: ISousFamille = { id: 123 };
        const sousFamille2: ISousFamille = { id: 456 };
        expectedResult = service.addSousFamilleToCollectionIfMissing([], sousFamille, sousFamille2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousFamille);
        expect(expectedResult).toContain(sousFamille2);
      });

      it('should accept null and undefined values', () => {
        const sousFamille: ISousFamille = { id: 123 };
        expectedResult = service.addSousFamilleToCollectionIfMissing([], null, sousFamille, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousFamille);
      });

      it('should return initial array if no SousFamille is added', () => {
        const sousFamilleCollection: ISousFamille[] = [{ id: 123 }];
        expectedResult = service.addSousFamilleToCollectionIfMissing(sousFamilleCollection, undefined, null);
        expect(expectedResult).toEqual(sousFamilleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
