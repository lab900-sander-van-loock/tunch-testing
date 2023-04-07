import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BeerBottleDetailComponent } from './beer-bottle-detail.component';

describe('BeerBottle Management Detail Component', () => {
  let comp: BeerBottleDetailComponent;
  let fixture: ComponentFixture<BeerBottleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BeerBottleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ beerBottle: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(BeerBottleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BeerBottleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load beerBottle on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.beerBottle).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
