import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBeerBottle } from '../beer-bottle.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../beer-bottle.test-samples';

import { BeerBottleService, RestBeerBottle } from './beer-bottle.service';

const requireRestSample: RestBeerBottle = {
  ...sampleWithRequiredData,
  expirationDate: sampleWithRequiredData.expirationDate?.format(DATE_FORMAT),
};

describe('BeerBottle Service', () => {
  let service: BeerBottleService;
  let httpMock: HttpTestingController;
  let expectedResult: IBeerBottle | IBeerBottle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BeerBottleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BeerBottle', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const beerBottle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(beerBottle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BeerBottle', () => {
      const beerBottle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(beerBottle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BeerBottle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BeerBottle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BeerBottle', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBeerBottleToCollectionIfMissing', () => {
      it('should add a BeerBottle to an empty array', () => {
        const beerBottle: IBeerBottle = sampleWithRequiredData;
        expectedResult = service.addBeerBottleToCollectionIfMissing([], beerBottle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beerBottle);
      });

      it('should not add a BeerBottle to an array that contains it', () => {
        const beerBottle: IBeerBottle = sampleWithRequiredData;
        const beerBottleCollection: IBeerBottle[] = [
          {
            ...beerBottle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBeerBottleToCollectionIfMissing(beerBottleCollection, beerBottle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BeerBottle to an array that doesn't contain it", () => {
        const beerBottle: IBeerBottle = sampleWithRequiredData;
        const beerBottleCollection: IBeerBottle[] = [sampleWithPartialData];
        expectedResult = service.addBeerBottleToCollectionIfMissing(beerBottleCollection, beerBottle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beerBottle);
      });

      it('should add only unique BeerBottle to an array', () => {
        const beerBottleArray: IBeerBottle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const beerBottleCollection: IBeerBottle[] = [sampleWithRequiredData];
        expectedResult = service.addBeerBottleToCollectionIfMissing(beerBottleCollection, ...beerBottleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const beerBottle: IBeerBottle = sampleWithRequiredData;
        const beerBottle2: IBeerBottle = sampleWithPartialData;
        expectedResult = service.addBeerBottleToCollectionIfMissing([], beerBottle, beerBottle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(beerBottle);
        expect(expectedResult).toContain(beerBottle2);
      });

      it('should accept null and undefined values', () => {
        const beerBottle: IBeerBottle = sampleWithRequiredData;
        expectedResult = service.addBeerBottleToCollectionIfMissing([], null, beerBottle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(beerBottle);
      });

      it('should return initial array if no BeerBottle is added', () => {
        const beerBottleCollection: IBeerBottle[] = [sampleWithRequiredData];
        expectedResult = service.addBeerBottleToCollectionIfMissing(beerBottleCollection, undefined, null);
        expect(expectedResult).toEqual(beerBottleCollection);
      });
    });

    describe('compareBeerBottle', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBeerBottle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareBeerBottle(entity1, entity2);
        const compareResult2 = service.compareBeerBottle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareBeerBottle(entity1, entity2);
        const compareResult2 = service.compareBeerBottle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareBeerBottle(entity1, entity2);
        const compareResult2 = service.compareBeerBottle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
