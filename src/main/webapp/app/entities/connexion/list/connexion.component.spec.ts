import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ConnexionService } from '../service/connexion.service';

import { ConnexionComponent } from './connexion.component';

describe('Connexion Management Component', () => {
  let comp: ConnexionComponent;
  let fixture: ComponentFixture<ConnexionComponent>;
  let service: ConnexionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ConnexionComponent],
    })
      .overrideTemplate(ConnexionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConnexionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConnexionService);

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
    expect(comp.connexions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
