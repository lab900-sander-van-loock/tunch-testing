import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Fridge e2e test', () => {
  const fridgePageUrl = '/fridge';
  const fridgePageUrlPattern = new RegExp('/fridge(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fridgeSample = { name: 'Buckinghamshire user-centric' };

  let fridge;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fridges+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fridges').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fridges/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fridge) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fridges/${fridge.id}`,
      }).then(() => {
        fridge = undefined;
      });
    }
  });

  it('Fridges menu should load Fridges page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fridge');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Fridge').should('exist');
    cy.url().should('match', fridgePageUrlPattern);
  });

  describe('Fridge page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fridgePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Fridge page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fridge/new$'));
        cy.getEntityCreateUpdateHeading('Fridge');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fridgePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fridges',
          body: fridgeSample,
        }).then(({ body }) => {
          fridge = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fridges+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fridge],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fridgePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Fridge page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fridge');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fridgePageUrlPattern);
      });

      it('edit button click should load edit Fridge page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fridge');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fridgePageUrlPattern);
      });

      it('edit button click should load edit Fridge page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fridge');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fridgePageUrlPattern);
      });

      it('last delete button click should delete instance of Fridge', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fridge').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fridgePageUrlPattern);

        fridge = undefined;
      });
    });
  });

  describe('new Fridge page', () => {
    beforeEach(() => {
      cy.visit(`${fridgePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Fridge');
    });

    it('should create an instance of Fridge', () => {
      cy.get(`[data-cy="name"]`).type('pink').should('have.value', 'pink');

      cy.get(`[data-cy="location"]`).type('International content-based').should('have.value', 'International content-based');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        fridge = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', fridgePageUrlPattern);
    });
  });
});
