import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFamille, Famille } from '../famille.model';

import { FamilleService } from './famille.service';

describe('Famille Service', () => {
  let service: FamilleService;
  let httpMock: HttpTestingController;
  let elemDefault: IFamille;
  let expectedResult: IFamille | IFamille[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FamilleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      iDFamille: 0,
      famille: 'AAAAAAA',
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

    it('should create a Famille', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Famille()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Famille', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDFamille: 1,
          famille: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Famille', () => {
      const patchObject = Object.assign(
        {
          iDFamille: 1,
        },
        new Famille()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Famille', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDFamille: 1,
          famille: 'BBBBBB',
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

    it('should delete a Famille', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFamilleToCollectionIfMissing', () => {
      it('should add a Famille to an empty array', () => {
        const famille: IFamille = { id: 123 };
        expectedResult = service.addFamilleToCollectionIfMissing([], famille);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(famille);
      });

      it('should not add a Famille to an array that contains it', () => {
        const famille: IFamille = { id: 123 };
        const familleCollection: IFamille[] = [
          {
            ...famille,
          },
          { id: 456 },
        ];
        expectedResult = service.addFamilleToCollectionIfMissing(familleCollection, famille);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Famille to an array that doesn't contain it", () => {
        const famille: IFamille = { id: 123 };
        const familleCollection: IFamille[] = [{ id: 456 }];
        expectedResult = service.addFamilleToCollectionIfMissing(familleCollection, famille);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(famille);
      });

      it('should add only unique Famille to an array', () => {
        const familleArray: IFamille[] = [{ id: 123 }, { id: 456 }, { id: 36412 }];
        const familleCollection: IFamille[] = [{ id: 123 }];
        expectedResult = service.addFamilleToCollectionIfMissing(familleCollection, ...familleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const famille: IFamille = { id: 123 };
        const famille2: IFamille = { id: 456 };
        expectedResult = service.addFamilleToCollectionIfMissing([], famille, famille2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(famille);
        expect(expectedResult).toContain(famille2);
      });

      it('should accept null and undefined values', () => {
        const famille: IFamille = { id: 123 };
        expectedResult = service.addFamilleToCollectionIfMissing([], null, famille, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(famille);
      });

      it('should return initial array if no Famille is added', () => {
        const familleCollection: IFamille[] = [{ id: 123 }];
        expectedResult = service.addFamilleToCollectionIfMissing(familleCollection, undefined, null);
        expect(expectedResult).toEqual(familleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
