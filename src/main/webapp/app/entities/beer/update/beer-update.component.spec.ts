import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeerFormService } from './beer-form.service';
import { BeerService } from '../service/beer.service';
import { IBeer } from '../beer.model';

import { BeerUpdateComponent } from './beer-update.component';

describe('Beer Management Update Component', () => {
  let comp: BeerUpdateComponent;
  let fixture: ComponentFixture<BeerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beerFormService: BeerFormService;
  let beerService: BeerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeerUpdateComponent],
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
      .overrideTemplate(BeerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beerFormService = TestBed.inject(BeerFormService);
    beerService = TestBed.inject(BeerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const beer: IBeer = { id: 456 };

      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      expect(comp.beer).toEqual(beer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeer>>();
      const beer = { id: 123 };
      jest.spyOn(beerFormService, 'getBeer').mockReturnValue(beer);
      jest.spyOn(beerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beer }));
      saveSubject.complete();

      // THEN
      expect(beerFormService.getBeer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(beerService.update).toHaveBeenCalledWith(expect.objectContaining(beer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeer>>();
      const beer = { id: 123 };
      jest.spyOn(beerFormService, 'getBeer').mockReturnValue({ id: null });
      jest.spyOn(beerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beer }));
      saveSubject.complete();

      // THEN
      expect(beerFormService.getBeer).toHaveBeenCalled();
      expect(beerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeer>>();
      const beer = { id: 123 };
      jest.spyOn(beerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
