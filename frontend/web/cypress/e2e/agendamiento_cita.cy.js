describe('SIKALMA E2E - Flujo de Agendamiento de Cita Psicológica', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200');
  });

  it('permite navegar al formulario de reserva y registrar una cita válida', () => {
    cy.contains('button', 'Agendar Cita').click();
    cy.contains('Reserva de Cita Psicológica').should('be.visible');

    // Llenar datos del paciente
    cy.get('input[name="numeroDocumento"]').type('74859612');
    cy.get('input[name="nombres"]').type('Rodrigo');
    cy.get('input[name="apellidos"]').type('Navarro Soto');
    cy.get('input[name="telefono"]').type('951234567');
    cy.get('input[name="correo"]').type('rodrigo.navarro@gmail.com');

    // Seleccionar fecha y hora
    cy.get('input[name="fecha"]').type('2026-10-20');
    cy.get('select[name="hora"]').select('10:00');

    // Confirmar reserva
    cy.contains('button', 'Confirmar Reserva de Cita').click();

    // Validar mensaje de éxito
    cy.contains('¡Cita registrada con éxito para Rodrigo Navarro Soto!').should('be.visible');
  });

  it('bloquea el registro si el DNI es inválido (menos de 8 dígitos)', () => {
    cy.contains('button', 'Agendar Cita').click();

    cy.get('input[name="numeroDocumento"]').type('12345'); // DNI corto
    cy.get('input[name="nombres"]').type('María');
    cy.get('input[name="apellidos"]').type('López');
    cy.get('input[name="telefono"]').type('987654321');
    cy.get('input[name="fecha"]').type('2026-10-21');

    cy.contains('button', 'Confirmar Reserva de Cita').click();

    cy.contains('El DNI debe contener exactamente 8 dígitos numéricos.').should('be.visible');
  });
});
