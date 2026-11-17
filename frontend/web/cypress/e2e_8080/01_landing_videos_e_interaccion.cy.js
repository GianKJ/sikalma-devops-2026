describe('SIKALMA 8080 - Landing Animado, Videos y Secciones Interactivas', () => {
  beforeEach(() => {
    cy.clearCookies();
    cy.visit('/');
  });

  it('carga correctamente el video horizontal del hero y el reel vertical SIKALMA KIDS', () => {
    cy.contains('Centro Psicológico SIKALMA').should('be.visible');

    cy.get('#hero-bg-video source')
      .should('have.attr', 'src')
      .and('include', 'sikalma-consultorio-hero.mp4');

    cy.request('/video/sikalma-consultorio-hero.mp4').its('status').should('eq', 200);

    cy.get('#hero-video-toggle')
      .should('be.visible')
      .and('contain', 'Pausar')
      .click()
      .should('contain', 'Reproducir');

    cy.get('#nosotros').scrollIntoView();
    cy.get('#kids-vertical-video source')
      .should('have.attr', 'src')
      .and('include', 'sikalma-kids-vertical.mp4');

    cy.request('/video/sikalma-kids-vertical.mp4').its('status').should('eq', 200);

    cy.get('#kids-sound-toggle')
      .should('contain', 'Activar audio')
      .click()
      .should('contain', 'Silenciar');
  });

  it('filtra servicios por modalidad y despliega el acordeon de preguntas frecuentes', () => {
    cy.get('#servicios').scrollIntoView();
    cy.get('[data-service-modality]').should('have.length.at.least', 4);

    cy.get('[data-service-filter="VIRTUAL"]').click();
    cy.get('[data-service-filter="VIRTUAL"]').should('have.class', 'is-active');
    cy.get('[data-service-modality="PRESENCIAL"]').should('have.class', 'is-hidden');

    cy.get('[data-service-filter="TODOS"]').click();
    cy.get('[data-service-modality].is-hidden').should('have.length', 0);

    cy.get('#preguntas').scrollIntoView();
    cy.get('[data-faq-trigger]').first().click();
    cy.get('[data-faq-item]').first().should('have.class', 'is-open');
    cy.get('[data-faq-trigger]').first().should('have.attr', 'aria-expanded', 'true');
  });

  it('navega desde la landing hasta la pantalla de acceso del equipo', () => {
    cy.get('#contacto').scrollIntoView();
    cy.contains('a', 'Acceso del equipo').click();
    cy.url().should('include', '/login');
    cy.contains('h1', 'Iniciar sesión').should('be.visible');
    cy.get('[data-demo-user="admin"]').should('be.visible');
  });
});
