import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SousFamilleService } from '../service/sous-famille.service';

import { SousFamilleComponent } from './sous-famille.component';

describe('SousFamille Management Component', () => {
  let comp: SousFamilleComponent;
  let fixture: ComponentFixture<SousFamilleComponent>;
  let service: SousFamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousFamilleComponent],
    })
      .overrideTemplate(SousFamilleComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousFamilleComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SousFamilleService);

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
    expect(comp.sousFamilles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
