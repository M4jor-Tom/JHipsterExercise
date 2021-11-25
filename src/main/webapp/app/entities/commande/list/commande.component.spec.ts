import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CommandeService } from '../service/commande.service';

import { CommandeComponent } from './commande.component';

describe('Commande Management Component', () => {
  let comp: CommandeComponent;
  let fixture: ComponentFixture<CommandeComponent>;
  let service: CommandeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CommandeComponent],
    })
      .overrideTemplate(CommandeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CommandeService);

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
    expect(comp.commandes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
