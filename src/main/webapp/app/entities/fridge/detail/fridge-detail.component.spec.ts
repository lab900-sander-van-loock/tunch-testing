import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FridgeDetailComponent } from './fridge-detail.component';

describe('Fridge Management Detail Component', () => {
  let comp: FridgeDetailComponent;
  let fixture: ComponentFixture<FridgeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FridgeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fridge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FridgeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FridgeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fridge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fridge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
