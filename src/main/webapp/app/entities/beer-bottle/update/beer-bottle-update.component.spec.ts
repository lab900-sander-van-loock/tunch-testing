import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeerBottleFormService } from './beer-bottle-form.service';
import { BeerBottleService } from '../service/beer-bottle.service';
import { IBeerBottle } from '../beer-bottle.model';
import { IBeer } from 'app/entities/beer/beer.model';
import { BeerService } from 'app/entities/beer/service/beer.service';
import { IFridge } from 'app/entities/fridge/fridge.model';
import { FridgeService } from 'app/entities/fridge/service/fridge.service';

import { BeerBottleUpdateComponent } from './beer-bottle-update.component';

describe('BeerBottle Management Update Component', () => {
  let comp: BeerBottleUpdateComponent;
  let fixture: ComponentFixture<BeerBottleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beerBottleFormService: BeerBottleFormService;
  let beerBottleService: BeerBottleService;
  let beerService: BeerService;
  let fridgeService: FridgeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeerBottleUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BeerBottleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeerBottleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beerBottleFormService = TestBed.inject(BeerBottleFormService);
    beerBottleService = TestBed.inject(BeerBottleService);
    beerService = TestBed.inject(BeerService);
    fridgeService = TestBed.inject(FridgeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Beer query and add missing value', () => {
      const beerBottle: IBeerBottle = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const beer: IBeer = { id: 4150 };
      beerBottle.beer = beer;

      const beerCollection: IBeer[] = [{ id: 88161 }];
      jest.spyOn(beerService, 'query').mockReturnValue(of(new HttpResponse({ body: beerCollection })));
      const additionalBeers = [beer];
      const expectedCollection: IBeer[] = [...additionalBeers, ...beerCollection];
      jest.spyOn(beerService, 'addBeerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beerBottle });
      comp.ngOnInit();

      expect(beerService.query).toHaveBeenCalled();
      expect(beerService.addBeerToCollectionIfMissing).toHaveBeenCalledWith(
        beerCollection,
        ...additionalBeers.map(expect.objectContaining)
      );
      expect(comp.beersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Fridge query and add missing value', () => {
      const beerBottle: IBeerBottle = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const fridge: IFridge = { id: 54211 };
      beerBottle.fridge = fridge;

      const fridgeCollection: IFridge[] = [{ id: 5245 }];
      jest.spyOn(fridgeService, 'query').mockReturnValue(of(new HttpResponse({ body: fridgeCollection })));
      const additionalFridges = [fridge];
      const expectedCollection: IFridge[] = [...additionalFridges, ...fridgeCollection];
      jest.spyOn(fridgeService, 'addFridgeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beerBottle });
      comp.ngOnInit();

      expect(fridgeService.query).toHaveBeenCalled();
      expect(fridgeService.addFridgeToCollectionIfMissing).toHaveBeenCalledWith(
        fridgeCollection,
        ...additionalFridges.map(expect.objectContaining)
      );
      expect(comp.fridgesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const beerBottle: IBeerBottle = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const beer: IBeer = { id: 23370 };
      beerBottle.beer = beer;
      const fridge: IFridge = { id: 68969 };
      beerBottle.fridge = fridge;

      activatedRoute.data = of({ beerBottle });
      comp.ngOnInit();

      expect(comp.beersSharedCollection).toContain(beer);
      expect(comp.fridgesSharedCollection).toContain(fridge);
      expect(comp.beerBottle).toEqual(beerBottle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeerBottle>>();
      const beerBottle = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(beerBottleFormService, 'getBeerBottle').mockReturnValue(beerBottle);
      jest.spyOn(beerBottleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beerBottle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beerBottle }));
      saveSubject.complete();

      // THEN
      expect(beerBottleFormService.getBeerBottle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(beerBottleService.update).toHaveBeenCalledWith(expect.objectContaining(beerBottle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeerBottle>>();
      const beerBottle = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(beerBottleFormService, 'getBeerBottle').mockReturnValue({ id: null });
      jest.spyOn(beerBottleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beerBottle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beerBottle }));
      saveSubject.complete();

      // THEN
      expect(beerBottleFormService.getBeerBottle).toHaveBeenCalled();
      expect(beerBottleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeerBottle>>();
      const beerBottle = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(beerBottleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beerBottle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beerBottleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBeer', () => {
      it('Should forward to beerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(beerService, 'compareBeer');
        comp.compareBeer(entity, entity2);
        expect(beerService.compareBeer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFridge', () => {
      it('Should forward to fridgeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fridgeService, 'compareFridge');
        comp.compareFridge(entity, entity2);
        expect(fridgeService.compareFridge).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
