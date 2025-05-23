import { HomePageComponent } from '../src/app/pages/home.page';
import { UnosNikotinaControllerService } from '../src/app/api/api/unosNikotinaController.service';
import { of } from 'rxjs';

describe('HomePageComponent', () => {
  const mockData = [
    {
      datum: new Date().toISOString(),
      kolicina: 2,
      nikotinSadrzaj: 5,
      opisProizvoda: 'Test proizvod',
    },
    {
      datum: new Date().toISOString(),
      kolicina: 1,
      nikotinSadrzaj: 8,
      opisProizvoda: 'Test proizvod 2',
    },
  ];

  beforeEach(() => {
    cy.mount(HomePageComponent, {
      providers: [
        {
          provide: UnosNikotinaControllerService,
          useValue: {
            unosiZaKorisnikaURasponu: () => of(mockData),
          },
        },
      ],
    });
  });

  it("should render today's total nicotine intake", () => {
    cy.contains('mg');
    cy.contains('danas');
    cy.get('h2').should('contain.text', '18'); // 2*5 + 1*8 = 18
  });

  it('should render the weekly nicotine chart', () => {
    cy.contains('Tjedni unos nikotina');
    cy.get('canvas').should('exist');
  });
});
