describe('SIKALMA 8080 - Citas, Prevencion de Cruces y Atencion Clinica', () => {
  const loginAs = (demoUser) => {
    cy.clearCookies();
    cy.visit('/login');
    cy.get(`[data-demo-user="${demoUser}"]`).click();
    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();
    cy.get('body').then(($body) => {
      if ($body.find('#welcome-skip-btn').length > 0) {
        cy.get('#welcome-skip-btn').click();
      }
    });
  };

  it('registra cita, detecta traslape de horario, sugiere alternativa libre y avanza estados', () => {
    const sufijo = Date.now().toString().slice(-4);
    const motivoUnico = `Evaluacion E2E ${sufijo}`;
    const fechaFuturo = `2027-03-${String((Number(sufijo) % 18) + 10).padStart(2, '0')}`;

    loginAs('admin');
    cy.visit('/admin/citas/nuevo');

    cy.get('#pacienteId option').eq(1).then(($opt) => cy.get('#pacienteId').select($opt.val()));
    cy.get('#psicologoId option').eq(1).then(($opt) => cy.get('#psicologoId').select($opt.val()));
    cy.get('#servicioId option').eq(1).then(($opt) => cy.get('#servicioId').select($opt.val()));
    cy.get('#modalidad').select('PRESENCIAL');
    cy.get('#fecha').clear().type(fechaFuturo);
    cy.get('#horaInicio').clear().type('11:00');
    cy.get('#estado').select('PENDIENTE');
    cy.get('#motivoConsulta').clear().type(motivoUnico);
    cy.contains('button[type="submit"]', 'Guardar cita').click();

    cy.url().should('match', /\/admin\/citas$/);
    cy.get('.alert--success').should('be.visible');
    cy.contains('tr', motivoUnico).should('contain', 'Pendiente');

    // Emitir comprobante de la cita recien creada
    cy.contains('tr', motivoUnico).contains('a', 'Comprobante').click();
    cy.contains('h1', 'Comprobante de Cita').should('be.visible');
    cy.contains('a', 'Volver a citas').click();

    // Intentar registrar un horario traslapado para validar sugerencias automaticas
    cy.visit('/admin/citas/nuevo');
    cy.get('#pacienteId option').eq(1).then(($opt) => cy.get('#pacienteId').select($opt.val()));
    cy.get('#psicologoId option').eq(1).then(($opt) => cy.get('#psicologoId').select($opt.val()));
    cy.get('#servicioId option').eq(1).then(($opt) => cy.get('#servicioId').select($opt.val()));
    cy.get('#fecha').clear().type(fechaFuturo);
    cy.get('#horaInicio').clear().type('11:20');
    cy.get('#motivoConsulta').clear().type(`${motivoUnico} reprogramada`);
    cy.contains('button[type="submit"]', 'Guardar cita').click();

    cy.get('.conflict-box').should('be.visible');
    cy.get('.slot-chip[data-suggested-time]').should('have.length.at.least', 1);
    cy.get('.slot-chip[data-suggested-time]').first().click();
    cy.contains('button[type="submit"]', 'Guardar cita').click();

    cy.url().should('match', /\/admin\/citas$/);
    cy.contains('tr', `${motivoUnico} reprogramada`).should('be.visible');

    // Cambiar estado de la cita inicial: Pendiente -> Confirmar -> En sala
    cy.contains('tr', motivoUnico).contains('button', 'Confirmar').click();
    cy.contains('tr', motivoUnico).should('contain', 'Confirmada');
    cy.contains('tr', motivoUnico).contains('button', 'En sala').click();
    cy.contains('tr', motivoUnico).should('contain', 'En sala');
  });

  it('permite a la psicologa registrar nota de evolucion clinica confidencial', () => {
    const notaTexto = `Evolucion E2E ${Date.now()}`;
    loginAs('maryory');
    cy.url().should('include', '/profesional/inicio');

    cy.get('table tbody tr a').first().click();
    cy.url().should('include', '/atencion');

    cy.get('#diagnosticoReferencial').clear().type('Avance terapeutico E2E');
    cy.get('#estadoClinico').select('ATENDIDA');
    cy.get('#notaEvolucion').clear().type(notaTexto);
    cy.contains('button[type="submit"]', 'Guardar atención clínica').click();

    cy.url().should('include', '/profesional/inicio');
    cy.get('.alert--success').should('be.visible');
    cy.contains('td', 'Avance terapeutico E2E').should('be.visible');
  });
});
