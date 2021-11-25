import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConnexion, Connexion } from '../connexion.model';

import { ConnexionService } from './connexion.service';

describe('Connexion Service', () => {
  let service: ConnexionService;
  let httpMock: HttpTestingController;
  let elemDefault: IConnexion;
  let expectedResult: IConnexion | IConnexion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConnexionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      iDConnexion: 0,
      username: 'AAAAAAA',
      password: 'AAAAAAA',
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

    it('should create a Connexion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Connexion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Connexion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDConnexion: 1,
          username: 'BBBBBB',
          password: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Connexion', () => {
      const patchObject = Object.assign(
        {
          username: 'BBBBBB',
        },
        new Connexion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Connexion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDConnexion: 1,
          username: 'BBBBBB',
          password: 'BBBBBB',
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

    it('should delete a Connexion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConnexionToCollectionIfMissing', () => {
      it('should add a Connexion to an empty array', () => {
        const connexion: IConnexion = { id: 123 };
        expectedResult = service.addConnexionToCollectionIfMissing([], connexion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(connexion);
      });

      it('should not add a Connexion to an array that contains it', () => {
        const connexion: IConnexion = { id: 123 };
        const connexionCollection: IConnexion[] = [
          {
            ...connexion,
          },
          { id: 456 },
        ];
        expectedResult = service.addConnexionToCollectionIfMissing(connexionCollection, connexion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Connexion to an array that doesn't contain it", () => {
        const connexion: IConnexion = { id: 123 };
        const connexionCollection: IConnexion[] = [{ id: 456 }];
        expectedResult = service.addConnexionToCollectionIfMissing(connexionCollection, connexion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(connexion);
      });

      it('should add only unique Connexion to an array', () => {
        const connexionArray: IConnexion[] = [{ id: 123 }, { id: 456 }, { id: 44370 }];
        const connexionCollection: IConnexion[] = [{ id: 123 }];
        expectedResult = service.addConnexionToCollectionIfMissing(connexionCollection, ...connexionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const connexion: IConnexion = { id: 123 };
        const connexion2: IConnexion = { id: 456 };
        expectedResult = service.addConnexionToCollectionIfMissing([], connexion, connexion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(connexion);
        expect(expectedResult).toContain(connexion2);
      });

      it('should accept null and undefined values', () => {
        const connexion: IConnexion = { id: 123 };
        expectedResult = service.addConnexionToCollectionIfMissing([], null, connexion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(connexion);
      });

      it('should return initial array if no Connexion is added', () => {
        const connexionCollection: IConnexion[] = [{ id: 123 }];
        expectedResult = service.addConnexionToCollectionIfMissing(connexionCollection, undefined, null);
        expect(expectedResult).toEqual(connexionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
