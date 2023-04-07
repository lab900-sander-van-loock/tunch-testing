import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFridge } from '../fridge.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fridge.test-samples';

import { FridgeService } from './fridge.service';

const requireRestSample: IFridge = {
  ...sampleWithRequiredData,
};

describe('Fridge Service', () => {
  let service: FridgeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFridge | IFridge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FridgeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Fridge', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fridge = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fridge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Fridge', () => {
      const fridge = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fridge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Fridge', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Fridge', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Fridge', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFridgeToCollectionIfMissing', () => {
      it('should add a Fridge to an empty array', () => {
        const fridge: IFridge = sampleWithRequiredData;
        expectedResult = service.addFridgeToCollectionIfMissing([], fridge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fridge);
      });

      it('should not add a Fridge to an array that contains it', () => {
        const fridge: IFridge = sampleWithRequiredData;
        const fridgeCollection: IFridge[] = [
          {
            ...fridge,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFridgeToCollectionIfMissing(fridgeCollection, fridge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Fridge to an array that doesn't contain it", () => {
        const fridge: IFridge = sampleWithRequiredData;
        const fridgeCollection: IFridge[] = [sampleWithPartialData];
        expectedResult = service.addFridgeToCollectionIfMissing(fridgeCollection, fridge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fridge);
      });

      it('should add only unique Fridge to an array', () => {
        const fridgeArray: IFridge[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fridgeCollection: IFridge[] = [sampleWithRequiredData];
        expectedResult = service.addFridgeToCollectionIfMissing(fridgeCollection, ...fridgeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fridge: IFridge = sampleWithRequiredData;
        const fridge2: IFridge = sampleWithPartialData;
        expectedResult = service.addFridgeToCollectionIfMissing([], fridge, fridge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fridge);
        expect(expectedResult).toContain(fridge2);
      });

      it('should accept null and undefined values', () => {
        const fridge: IFridge = sampleWithRequiredData;
        expectedResult = service.addFridgeToCollectionIfMissing([], null, fridge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fridge);
      });

      it('should return initial array if no Fridge is added', () => {
        const fridgeCollection: IFridge[] = [sampleWithRequiredData];
        expectedResult = service.addFridgeToCollectionIfMissing(fridgeCollection, undefined, null);
        expect(expectedResult).toEqual(fridgeCollection);
      });
    });

    describe('compareFridge', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFridge(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFridge(entity1, entity2);
        const compareResult2 = service.compareFridge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFridge(entity1, entity2);
        const compareResult2 = service.compareFridge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFridge(entity1, entity2);
        const compareResult2 = service.compareFridge(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
