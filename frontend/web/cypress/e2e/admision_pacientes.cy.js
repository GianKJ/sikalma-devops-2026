describe('SIKALMA E2E - Admisión y Gestión de Pacientes (Ley 29733)', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200');
  });

  it('permite acceder al panel de admisión, buscar y registrar un nuevo paciente', () => {
    cy.contains('button', 'Panel de Admisión').click();
    cy.contains('Gestión de Pacientes y Admisión').should('be.visible');

    // Buscar paciente existente
    cy.get('input[placeholder*="Buscar por DNI"]').type('74125896');
    cy.contains('Lucía Andrea Mendoza Carrillo').should('be.visible');
    cy.contains('Jorge Luis Huamán Rivera').should('not.exist');

    // Limpiar búsqueda y abrir formulario de nuevo paciente
    cy.get('input[placeholder*="Buscar por DNI"]').clear();
    cy.contains('button', '+ Nuevo Paciente').click();
    cy.contains('Ficha de Admisión de Paciente').should('be.visible');

    // Registrar nuevo paciente
    cy.get('input[placeholder="78945612"]').type('88990011');
    cy.get('input[placeholder="Nombres"]').type('Gabriel');
    cy.get('input[placeholder="Apellidos"]').type('Mori Torres');
    cy.get('input[placeholder="951 234 567"]').type('944556677');
    cy.get('input[type="date"]').type('1992-07-18');

    cy.contains('button', 'Guardar Paciente').click();

    // Validar que aparece en la tabla
    cy.contains('Gabriel Mori Torres').should('be.visible');
    cy.contains('88990011').should('be.visible');
  });
});
