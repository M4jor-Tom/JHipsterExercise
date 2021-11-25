import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FamilleService } from '../service/famille.service';

import { FamilleComponent } from './famille.component';

describe('Famille Management Component', () => {
  let comp: FamilleComponent;
  let fixture: ComponentFixture<FamilleComponent>;
  let service: FamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FamilleComponent],
    })
      .overrideTemplate(FamilleComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilleComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FamilleService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.familles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
