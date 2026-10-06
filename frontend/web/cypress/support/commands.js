// Custom commands for Cypress
Cypress.Commands.add('loginAdmin', () => {
  cy.visit('/login');
  cy.get('input[name="username"]').type('admin');
  cy.get('input[name="password"]').type('Admin123!');
  cy.get('button[type="submit"]').click();
});
