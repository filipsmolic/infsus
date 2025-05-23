import { TestBed } from '@angular/core/testing';
import { NicotineHistoryPageComponent } from './nicotine-history.page';
import { provideHttpClient } from '@angular/common/http';
import {
  ProizvodControllerService,
  UnosNikotinaControllerService,
} from '../api';
import { of, throwError } from 'rxjs';
import { HttpEvent } from '@angular/common/http';

class MockProizvodControllerService {
  sviProizvodi() {
    return of([
      { idProizvod: 1, opis: 'Test Proizvod', nikotinSadrzaj: 5 },
      { idProizvod: 2, opis: 'Drugi Proizvod', nikotinSadrzaj: 10 },
    ]);
  }
}
class MockUnosNikotinaControllerService {
  unosiZaKorisnikaURasponu() {
    return of({
      content: [
        {
          idUnosNikotina: 1,
          datum: '2025-05-23T00:00:00',
          idProizvod: 1,
          opisProizvoda: 'Test Proizvod',
          kolicina: 2,
          nikotinSadrzaj: 5,
        },
      ],
      totalPages: 1,
      totalElements: 1,
      number: 0,
    });
  }
  azurirajUnosNikotina() {
    return of({});
  }
  obrisiUnosNikotina() {
    return of({});
  }
}

describe('NicotineHistoryPageComponent', () => {
  let component: NicotineHistoryPageComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NicotineHistoryPageComponent],
      providers: [
        provideHttpClient(),
        {
          provide: ProizvodControllerService,
          useClass: MockProizvodControllerService,
        },
        {
          provide: UnosNikotinaControllerService,
          useClass: MockUnosNikotinaControllerService,
        },
      ],
    }).compileComponents();
    const fixture = TestBed.createComponent(NicotineHistoryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch products on init', () => {
    expect(component.productsList.length).toBeGreaterThan(0);
  });

  it('should fetch history on init', () => {
    expect(component.history.length).toBeGreaterThan(0);
  });

  it('should open and close edit form', () => {
    const unos = component.history[0];
    component.editUnos(unos);
    expect(component.editFormVisible).toBeTrue();
    component.closeEditForm();
    expect(component.editFormVisible).toBeFalse();
  });

  it('should call saveEdit and close modal on success', () => {
    const unos = component.history[0];
    component.editUnos(unos);
    spyOn(
      component['unosNikotinaService'],
      'azurirajUnosNikotina'
    ).and.returnValue(of({} as HttpEvent<any>));
    component.saveEdit();
    expect(component.editFormVisible).toBeFalse();
  });

  it('should set history to empty on fetch error', () => {
    spyOn(
      component['unosNikotinaService'],
      'unosiZaKorisnikaURasponu'
    ).and.returnValue(throwError(() => new Error('fail')));
    component.fetchHistory();
    expect(component.history.length).toBe(0);
  });

  it('should call deleteUnos and refresh on success', () => {
    const unos = component.history[0];
    const spy = spyOn(
      component['unosNikotinaService'],
      'obrisiUnosNikotina'
    ).and.returnValue(of({} as HttpEvent<any>));
    spyOn(window, 'confirm').and.returnValue(true);
    component.deleteUnos(unos);
    expect(spy).toHaveBeenCalled();
  });

  it('should format date correctly', () => {
    expect(component.formatDate('2025-05-23T00:00:00')).toBe('23.05.2025.');
  });

  it('should handle pagination', () => {
    component.totalPages = 2;
    component.goToPage(1);
    expect(component.page).toBe(0);
  });
});
