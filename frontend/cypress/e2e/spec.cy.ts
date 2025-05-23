describe('Nicotine Tracker App', () => {
  beforeEach(() => {
    cy.visit('/home');
  });

  it('should navigate to nicotine intake page and submit a new intake', () => {
    cy.visit('/nicotine-intake');
    cy.contains('Unos nikotina');
    cy.get('select[name="product"]').select(2);
    cy.get('input[name="quantity"]').clear().type('2');
    cy.get('input[type="date"]')
      .invoke('val', new Date().toISOString().substring(0, 10))
      .trigger('change');
    cy.get('button[type="submit"]').click();
    cy.contains('Unos uspješno spremljen').should('exist');
  });

  it('should display nicotine history, filter, edit, and delete', () => {
    cy.visit('/nicotine-history');
    cy.contains('Povijest unosa nikotina');
    cy.get('table').should('exist');

    cy.get('input[name="dateFrom"]').type('2024-01-01');
    cy.get('input[name="dateTo"]').type('2025-12-31');
    cy.contains('Filtriraj').click();
    cy.get('table tbody tr').should('exist');

    cy.get('table tbody tr').first().find('button[title="Uredi"]').click();
    cy.get('input[name="editKolicina"]').clear().type('3');
    cy.get('button[type="submit"]').contains('Spremi').click();

    cy.get('table tbody tr').first().find('button[title="Obriši"]').click();
    cy.on('window:confirm', () => true);
  });
});
