describe('SIKALMA 8080 - Login Animado, Métricas y Control RBAC por Roles', () => {
  beforeEach(() => {
    cy.clearCookies();
  });

  it('muestra error legible ante credenciales invalidas', () => {
    cy.visit('/login');
    cy.get('#username').type('usuario_inexistente');
    cy.get('#password').type('clave_erronea');
    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();

    cy.url().should('include', '/login?error');
    cy.get('.form-message--error').should('be.visible');
  });

  it('inicia sesion como Administrador, muestra overlay animado, KPIs y graficos reales', () => {
    cy.visit('/login');
    cy.get('[data-demo-user="admin"]').click();
    cy.get('#toggle-password-btn').click().should('contain', 'Ocultar');
    cy.get('#password').should('have.attr', 'type', 'text');

    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();
    cy.url().should('include', '/admin/inicio');

    cy.get('#welcome-overlay')
      .should('be.visible')
      .and('have.class', 'welcome-overlay--ADMINISTRADOR');
    cy.contains('#welcome-title', 'Bienvenido(a)').should('be.visible');
    cy.get('#welcome-skip-btn').click();
    cy.get('#welcome-overlay').should('have.class', 'is-hidden');

    cy.get('[data-kpi-target]').should('have.length', 4);
    cy.get('.bar-fill[data-bar-value]').should('have.length.at.least', 5);
    cy.get('#donut-modalidad').should('be.visible');
    cy.get('#proximas-titulo').scrollIntoView().should('be.visible');

    // Al recargar la vista, la bienvenida no vuelve a aparecer
    cy.reload();
    cy.get('#welcome-overlay').should('not.exist');
  });

  it('inicia sesion como Recepcionista con su bienvenida y restringe rutas administrativas', () => {
    cy.visit('/login');
    cy.get('[data-demo-user="recepcion"]').click();
    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();

    cy.url().should('include', '/recepcion/inicio');
    cy.get('#welcome-overlay').should('have.class', 'welcome-overlay--RECEPCIONISTA');
    cy.get('#welcome-skip-btn').click();

    cy.contains('Recepción y Agenda SIKALMA').should('be.visible');
    cy.visit('/recepcion/citas');
    cy.contains('h1', 'Gestión de citas').should('be.visible');

    cy.request({ url: '/admin/usuarios', failOnStatusCode: false }).its('status').should('eq', 403);
    cy.request({ url: '/profesional/inicio', failOnStatusCode: false }).its('status').should('eq', 403);
  });

  it('inicia sesion como Psicologa y Fisioterapeuta con agenda aislada', () => {
    cy.visit('/login');
    cy.get('[data-demo-user="maryory"]').click();
    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();

    cy.url().should('include', '/profesional/inicio');
    cy.get('#welcome-overlay').should('have.class', 'welcome-overlay--PSICOLOGO');
    cy.get('#welcome-skip-btn').click();
    cy.contains('Mi Agenda').should('be.visible');
    cy.request({ url: '/admin/citas', failOnStatusCode: false }).its('status').should('eq', 403);

    cy.contains('button', 'Cerrar sesión').click();
    cy.url().should('include', 'sesion-cerrada');

    cy.visit('/login');
    cy.get('[data-demo-user="antony"]').click();
    cy.contains('button[type="submit"]', 'Ingresar al sistema').click();
    cy.url().should('include', '/profesional/inicio');
    cy.get('#welcome-overlay').should('have.class', 'welcome-overlay--FISIOTERAPEUTA');
  });
});
