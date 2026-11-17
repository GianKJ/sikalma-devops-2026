describe('SIKALMA 8080 - Registro de Usuarios, Nuevo Login y Catalogos', () => {
  const loginAsAdmin = () => {
    cy.clearCookies();
    cy.visit('/login');
    cy.get('[data-demo-user="admin"]').click();
    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();
    cy.get('body').then(($body) => {
      if ($body.find('#welcome-skip-btn').length > 0) {
        cy.get('#welcome-skip-btn').click();
      }
    });
  };

  it('crea un usuario nuevo en Administracion e inicia sesion inmediatamente con sus credenciales', () => {
    const sufijo = Date.now().toString().slice(-5);
    const nuevoUsername = `recep_e2e_${sufijo}`;
    const claveNueva = 'RecepE2E123!';

    loginAsAdmin();
    cy.visit('/admin/usuarios/nuevo');

    cy.get('#username').type(nuevoUsername);
    cy.get('#rol').select('RECEPCIONISTA');
    cy.get('#nombreCompleto').type(`Andrea E2E ${sufijo}`);
    cy.get('#correo').type(`${nuevoUsername}@sikalma.test`);
    cy.get('#password').type(claveNueva);
    cy.contains('button[type="submit"]', 'Guardar usuario').click();

    cy.url().should('match', /\/admin\/usuarios$/);
    cy.get('.alert--success').should('be.visible');
    cy.contains('tr', nuevoUsername).should('contain', 'Activo');

    // Cerrar sesion e ingresar con la cuenta recien creada
    cy.contains('button', 'Cerrar sesión').click();
    cy.visit('/login');
    cy.get('#username').type(nuevoUsername);
    cy.get('#password').type(claveNueva);
    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();

    cy.url().should('include', '/recepcion/inicio');
    cy.get('#welcome-overlay').should('be.visible');
    cy.contains('#welcome-title', `Andrea E2E ${sufijo}`).should('be.visible');
  });

  it('registra un paciente con DNI valido y verifica acceso a catalogos clinicos', () => {
    const dniUnico = `79${Date.now().toString().slice(-6)}`;
    loginAsAdmin();

    cy.visit('/admin/pacientes/nuevo');
    cy.get('#tipoDocumento').select('DNI');
    cy.get('#numeroDocumento').type(dniUnico);
    cy.get('#nombres').type('Mateo E2E');
    cy.get('#apellidos').type('Quispe Ramos');
    cy.get('#fechaNacimiento').type('1996-04-18');
    cy.get('#telefono').type('964112233');
    cy.get('#correo').type(`mateo.${dniUnico}@sikalma.test`);
    cy.get('input[name="consentimientoRegistrado"]').check();
    cy.contains('button[type="submit"]', 'Guardar paciente').click();

    cy.url().should('match', /\/admin\/pacientes$/);
    cy.contains('tr', dniUnico).should('contain', 'Mateo E2E');

    cy.visit('/admin/psicologos');
    cy.contains('h1', 'Gestión de psicólogos').should('be.visible');

    cy.visit('/admin/especialidades');
    cy.contains('h1', 'Gestión de especialidades').should('be.visible');

    cy.visit('/admin/servicios');
    cy.contains('h1', 'Gestión de servicios').should('be.visible');
  });
});
