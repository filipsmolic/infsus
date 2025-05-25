import { LoginPageComponent } from '../src/app/pages/login.page';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';

describe('LoginPageComponent', () => {
  beforeEach(() => {
    cy.mount(LoginPageComponent, {
      imports: [ReactiveFormsModule, RouterTestingModule],
    });
  });

  it('should render the login form', () => {
    cy.contains('NicotiNO');
    cy.contains('Login');
    cy.get('input[type="email"]').should('exist');
    cy.get('input[type="password"]').should('exist');
    cy.get('button[type="submit"]').should('exist');
  });

  it('should show validation error for invalid email', () => {
    cy.get('input[type="email"]').type('notanemail').blur();
    cy.contains('Unesite ispravnu email adresu.');
  });

  it('should show validation error for invalid password', () => {
    cy.get('input[type="password"]').type('short').blur();
    cy.contains('Lozinka mora imati barem 8 znakova.');
  });

  it('should enable submit when form is valid', () => {
    cy.get('input[type="email"]').type('test@example.com');
    cy.get('input[type="password"]').type('Valid123!');
    cy.get('button[type="submit"]').should('not.be.disabled');
  });
});
