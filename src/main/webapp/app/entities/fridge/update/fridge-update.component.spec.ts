import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FridgeFormService } from './fridge-form.service';
import { FridgeService } from '../service/fridge.service';
import { IFridge } from '../fridge.model';

import { FridgeUpdateComponent } from './fridge-update.component';

describe('Fridge Management Update Component', () => {
  let comp: FridgeUpdateComponent;
  let fixture: ComponentFixture<FridgeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fridgeFormService: FridgeFormService;
  let fridgeService: FridgeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FridgeUpdateComponent],
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
      .overrideTemplate(FridgeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FridgeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fridgeFormService = TestBed.inject(FridgeFormService);
    fridgeService = TestBed.inject(FridgeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fridge: IFridge = { id: 456 };

      activatedRoute.data = of({ fridge });
      comp.ngOnInit();

      expect(comp.fridge).toEqual(fridge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFridge>>();
      const fridge = { id: 123 };
      jest.spyOn(fridgeFormService, 'getFridge').mockReturnValue(fridge);
      jest.spyOn(fridgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fridge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fridge }));
      saveSubject.complete();

      // THEN
      expect(fridgeFormService.getFridge).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fridgeService.update).toHaveBeenCalledWith(expect.objectContaining(fridge));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFridge>>();
      const fridge = { id: 123 };
      jest.spyOn(fridgeFormService, 'getFridge').mockReturnValue({ id: null });
      jest.spyOn(fridgeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fridge: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fridge }));
      saveSubject.complete();

      // THEN
      expect(fridgeFormService.getFridge).toHaveBeenCalled();
      expect(fridgeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFridge>>();
      const fridge = { id: 123 };
      jest.spyOn(fridgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fridge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fridgeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
