import { NicotineHistoryPageComponent } from '../src/app/pages/nicotine-history.page';
import {
  UnosNikotinaControllerService,
  ProizvodControllerService,
} from '../src/app/api';
import { of } from 'rxjs';

describe('NicotineHistoryPageComponent', () => {
  const mockProducts = [
    { idProizvod: 1, opis: 'Test proizvod 1' },
    { idProizvod: 2, opis: 'Test proizvod 2' },
  ];
  const mockHistory = {
    content: [
      {
        idUnosNikotina: 1,
        datum: '2025-05-23T00:00:00',
        idProizvod: 1,
        opisProizvoda: 'Test proizvod 1',
        kolicina: 2,
        nikotinSadrzaj: 5,
      },
      {
        idUnosNikotina: 2,
        datum: '2025-05-22T00:00:00',
        idProizvod: 2,
        opisProizvoda: 'Test proizvod 2',
        kolicina: 1,
        nikotinSadrzaj: 8,
      },
    ],
    totalPages: 1,
    totalElements: 2,
    number: 0,
  };

  beforeEach(() => {
    cy.mount(NicotineHistoryPageComponent, {
      providers: [
        {
          provide: UnosNikotinaControllerService,
          useValue: {
            unosiZaKorisnikaURasponu: () => of(mockHistory),
            azurirajUnosNikotina: () => of({}),
            obrisiUnosNikotina: () => of({}),
          },
        },
        {
          provide: ProizvodControllerService,
          useValue: {
            sviProizvodi: () => of(mockProducts),
          },
        },
      ],
    });
  });

  it('should render the history table with data', () => {
    cy.contains('Povijest unosa nikotina');
    cy.get('table').should('exist');
    cy.get('tbody tr').should('have.length', 2);
    cy.contains('Test proizvod 1');
    cy.contains('Test proizvod 2');
  });

  it('should open and close the edit modal', () => {
    cy.get('button[title="Uredi"]').first().click();
    cy.contains('Uredi unos');
    cy.get('button').contains('Odustani').click();
    cy.get('form[name="editForm"]').should('not.exist');
  });

  it('should show pagination if more than one page', () => {
    cy.mount(NicotineHistoryPageComponent, {
      providers: [
        {
          provide: UnosNikotinaControllerService,
          useValue: {
            unosiZaKorisnikaURasponu: () =>
              of({ ...mockHistory, totalPages: 2 }),
            azurirajUnosNikotina: () => of({}),
            obrisiUnosNikotina: () => of({}),
          },
        },
        {
          provide: ProizvodControllerService,
          useValue: {
            sviProizvodi: () => of(mockProducts),
          },
        },
      ],
    });
    cy.get('button').contains('2');
  });
});
