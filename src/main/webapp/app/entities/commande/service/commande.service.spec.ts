import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommande, Commande } from '../commande.model';

import { CommandeService } from './commande.service';

describe('Commande Service', () => {
  let service: CommandeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommande;
  let expectedResult: ICommande | ICommande[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommandeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      iDCommande: 0,
      listeProduit: 0,
      somme: 0,
      addresseLivraison: 'AAAAAAA',
      modePaiement: 0,
      dateLivraison: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Commande', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.create(new Commande()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commande', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDCommande: 1,
          listeProduit: 1,
          somme: 1,
          addresseLivraison: 'BBBBBB',
          modePaiement: 1,
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commande', () => {
      const patchObject = Object.assign(
        {
          iDCommande: 1,
          listeProduit: 1,
          modePaiement: 1,
        },
        new Commande()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commande', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iDCommande: 1,
          listeProduit: 1,
          somme: 1,
          addresseLivraison: 'BBBBBB',
          modePaiement: 1,
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Commande', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommandeToCollectionIfMissing', () => {
      it('should add a Commande to an empty array', () => {
        const commande: ICommande = { id: 123 };
        expectedResult = service.addCommandeToCollectionIfMissing([], commande);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commande);
      });

      it('should not add a Commande to an array that contains it', () => {
        const commande: ICommande = { id: 123 };
        const commandeCollection: ICommande[] = [
          {
            ...commande,
          },
          { id: 456 },
        ];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, commande);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commande to an array that doesn't contain it", () => {
        const commande: ICommande = { id: 123 };
        const commandeCollection: ICommande[] = [{ id: 456 }];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, commande);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commande);
      });

      it('should add only unique Commande to an array', () => {
        const commandeArray: ICommande[] = [{ id: 123 }, { id: 456 }, { id: 82336 }];
        const commandeCollection: ICommande[] = [{ id: 123 }];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, ...commandeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commande: ICommande = { id: 123 };
        const commande2: ICommande = { id: 456 };
        expectedResult = service.addCommandeToCollectionIfMissing([], commande, commande2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commande);
        expect(expectedResult).toContain(commande2);
      });

      it('should accept null and undefined values', () => {
        const commande: ICommande = { id: 123 };
        expectedResult = service.addCommandeToCollectionIfMissing([], null, commande, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commande);
      });

      it('should return initial array if no Commande is added', () => {
        const commandeCollection: ICommande[] = [{ id: 123 }];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, undefined, null);
        expect(expectedResult).toEqual(commandeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
