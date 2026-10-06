describe('SIKALMA E2E - Navegación y Catálogo Profesional', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200');
  });

  it('permite navegar entre Inicio, Servicios y Directorio de Psicólogos', () => {
    // Portada
    cy.contains('Tu Refugio de Calma y Equilibrio Emocional').should('be.visible');

    // Navegar a Servicios
    cy.contains('button', 'Servicios').click();
    cy.contains('Nuestros Servicios Psicológicos').should('be.visible');
    cy.contains('Consulta Psicológica Individual').should('be.visible');
    cy.contains('Terapia de Pareja y Familiar').should('be.visible');

    // Navegar a Psicólogos
    cy.contains('button', 'Psicólogos').click();
    cy.contains('Nuestros Psicólogos Colegiados').should('be.visible');
    cy.contains('C.Ps.P. 28415').should('be.visible');
    cy.contains('Ps. Mariana Vásquez Benavides').should('be.visible');
  });
});
