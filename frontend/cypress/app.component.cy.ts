import { AppComponent } from '../src/app/app.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('AppComponent', () => {
  it('should render header and sidebar', () => {
    cy.mount(AppComponent, {
      componentProperties: { isLoginPage: false },
      imports: [RouterTestingModule],
    });
    cy.get('header').should('exist');
    cy.get('nav').should('exist');
  });

  it('should hide header and sidebar on login page', () => {
    cy.mount(AppComponent, {
      componentProperties: { isLoginPage: true },
      imports: [RouterTestingModule],
    });
    cy.get('header').should('not.exist');
    cy.get('nav').should('not.exist');
  });

  it('should have a NicotiNO link in the header', () => {
    cy.mount(AppComponent, {
      componentProperties: { isLoginPage: false },
      imports: [RouterTestingModule],
    });
    cy.get('header a').contains('NicotiNO').should('exist');
    cy.get('header a').should('have.attr', 'routerLink', '/home');
  });
});
