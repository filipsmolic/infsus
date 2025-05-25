import { NicotineIntakePageComponent } from '../src/app/pages/nicotine-intake.page';
import {
  ProizvodControllerService,
  UnosNikotinaControllerService,
} from '../src/app/api';
import { of } from 'rxjs';

describe('NicotineIntakePageComponent', () => {
  const mockProducts = [
    { idProizvod: 1, opis: 'Test proizvod 1', nikotinSadrzaj: 5 },
    { idProizvod: 2, opis: 'Test proizvod 2', nikotinSadrzaj: 8 },
  ];

  beforeEach(() => {
    cy.mount(NicotineIntakePageComponent, {
      providers: [
        {
          provide: ProizvodControllerService,
          useValue: {
            sviProizvodi: () => of(mockProducts),
          },
        },
        {
          provide: UnosNikotinaControllerService,
          useValue: {
            batchUnosNikotina: () => of({}),
          },
        },
      ],
    });
  });

  it('should render the form and calculate total nicotine', () => {
    cy.contains('Unos nikotina');
    cy.get('input[readonly][id="totalQuantityInput"]').should(
      'have.value',
      '0'
    );
    cy.get('select[name="product"]').first().select('1');
    cy.get('input[name="quantity"]').first().clear().type('2');
    cy.get('input[readonly][id="totalQuantityInput"]').should(
      'have.value',
      '100'
    );
  });

  it('should add and remove product rows', () => {
    cy.get('button').contains('Dodaj proizvod').click();
    cy.get('select[name^="product"]').should('have.length', 2);
    cy.get('button[aria-label="Ukloni proizvod"]').first().click();
    cy.get('select[name^="product"]').should('have.length', 1);
  });

  it('should show validation errors for required fields', () => {
    cy.get('input[name="date"]').clear().blur();
    cy.contains('Datum je obavezan.');
    cy.get('select[name^="product"]').first().focus().blur();
    cy.contains('Odabir proizvoda je obavezan.');
    cy.get('input[name^="quantity"]').first().clear().blur();
    cy.contains('Količina mora biti barem 1.');
  });

  it('should submit the form and show success message', () => {
    cy.get('select[name^="product"]').first().select('1');
    cy.get('input[name^="quantity"]').first().clear().type('2');
    cy.get('button[type="submit"]').click();
    cy.contains('Unos uspješno spremljen!');
  });
});
