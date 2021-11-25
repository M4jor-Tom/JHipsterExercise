import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICaracteristique, Caracteristique } from '../caracteristique.model';

import { CaracteristiqueService } from './caracteristique.service';

describe('Caracteristique Service', () => {
  let service: CaracteristiqueService;
  let httpMock: HttpTestingController;
  let elemDefault: ICaracteristique;
  let expectedResult: ICaracteristique | ICaracteristique[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CaracteristiqueService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      iDCaracteristique: 0,
      marque: 'AAAAAAA',
      modele: 'AAAAAAA',
      taille: 'AAAAAAA',
      couleur: 'AAAAAAA',
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

    it('should create a Caracteristique', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Caracteristique()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Caracteristique', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDCaracteristique: 1,
          marque: 'BBBBBB',
          modele: 'BBBBBB',
          taille: 'BBBBBB',
          couleur: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Caracteristique', () => {
      const patchObject = Object.assign(
        {
          marque: 'BBBBBB',
          modele: 'BBBBBB',
          couleur: 'BBBBBB',
        },
        new Caracteristique()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Caracteristique', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDCaracteristique: 1,
          marque: 'BBBBBB',
          modele: 'BBBBBB',
          taille: 'BBBBBB',
          couleur: 'BBBBBB',
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

    it('should delete a Caracteristique', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCaracteristiqueToCollectionIfMissing', () => {
      it('should add a Caracteristique to an empty array', () => {
        const caracteristique: ICaracteristique = { id: 123 };
        expectedResult = service.addCaracteristiqueToCollectionIfMissing([], caracteristique);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(caracteristique);
      });

      it('should not add a Caracteristique to an array that contains it', () => {
        const caracteristique: ICaracteristique = { id: 123 };
        const caracteristiqueCollection: ICaracteristique[] = [
          {
            ...caracteristique,
          },
          { id: 456 },
        ];
        expectedResult = service.addCaracteristiqueToCollectionIfMissing(caracteristiqueCollection, caracteristique);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Caracteristique to an array that doesn't contain it", () => {
        const caracteristique: ICaracteristique = { id: 123 };
        const caracteristiqueCollection: ICaracteristique[] = [{ id: 456 }];
        expectedResult = service.addCaracteristiqueToCollectionIfMissing(caracteristiqueCollection, caracteristique);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(caracteristique);
      });

      it('should add only unique Caracteristique to an array', () => {
        const caracteristiqueArray: ICaracteristique[] = [{ id: 123 }, { id: 456 }, { id: 79639 }];
        const caracteristiqueCollection: ICaracteristique[] = [{ id: 123 }];
        expectedResult = service.addCaracteristiqueToCollectionIfMissing(caracteristiqueCollection, ...caracteristiqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const caracteristique: ICaracteristique = { id: 123 };
        const caracteristique2: ICaracteristique = { id: 456 };
        expectedResult = service.addCaracteristiqueToCollectionIfMissing([], caracteristique, caracteristique2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(caracteristique);
        expect(expectedResult).toContain(caracteristique2);
      });

      it('should accept null and undefined values', () => {
        const caracteristique: ICaracteristique = { id: 123 };
        expectedResult = service.addCaracteristiqueToCollectionIfMissing([], null, caracteristique, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(caracteristique);
      });

      it('should return initial array if no Caracteristique is added', () => {
        const caracteristiqueCollection: ICaracteristique[] = [{ id: 123 }];
        expectedResult = service.addCaracteristiqueToCollectionIfMissing(caracteristiqueCollection, undefined, null);
        expect(expectedResult).toEqual(caracteristiqueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
