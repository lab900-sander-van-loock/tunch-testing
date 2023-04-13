describe('homepage', () => {
  beforeEach('open app', () => {
    cy.visit('http://localhost:9000')
  })

  describe('account menu', () => {
    beforeEach('click on account', () => {
      cy.get('#account-menu').click();
    })

    it('show dropdown menu', () => {
      cy.get('.dropdown-menu').should('be.visible');
    })

    it('click on register should open register page', () => {
      cy.get('[data-cy="register"]').click();
      cy.url().should('include', '/account/register');
    })

    it('click on register should open login page', () => {
      cy.get('[data-cy="login"]').click();
      cy.url().should('include', '/login');
    })

  })

  describe('register page', () => {
    beforeEach('go to register page', () => {
      cy.visit('http://localhost:9000/account/register')
    })

    it('email input', () => {
      cy.get('[data-cy="email"]').should('have.class', 'ng-invalid');
      cy.get('[data-cy="email"]').type('test@lab900.com');
    })

  })
})
