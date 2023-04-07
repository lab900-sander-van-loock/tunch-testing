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

describe('Beer e2e test', () => {
  const beerPageUrl = '/beer';
  const beerPageUrlPattern = new RegExp('/beer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const beerSample = { name: 'turquoise Creative tolerance', percentage: 43333 };

  let beer;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/beers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/beers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/beers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (beer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/beers/${beer.id}`,
      }).then(() => {
        beer = undefined;
      });
    }
  });

  it('Beers menu should load Beers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('beer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Beer').should('exist');
    cy.url().should('match', beerPageUrlPattern);
  });

  describe('Beer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(beerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Beer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/beer/new$'));
        cy.getEntityCreateUpdateHeading('Beer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/beers',
          body: beerSample,
        }).then(({ body }) => {
          beer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/beers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [beer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(beerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Beer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('beer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerPageUrlPattern);
      });

      it('edit button click should load edit Beer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Beer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerPageUrlPattern);
      });

      it('edit button click should load edit Beer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Beer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerPageUrlPattern);
      });

      it('last delete button click should delete instance of Beer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('beer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerPageUrlPattern);

        beer = undefined;
      });
    });
  });

  describe('new Beer page', () => {
    beforeEach(() => {
      cy.visit(`${beerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Beer');
    });

    it('should create an instance of Beer', () => {
      cy.get(`[data-cy="name"]`).type('feed Brand Awesome').should('have.value', 'feed Brand Awesome');

      cy.get(`[data-cy="brewery"]`).type('York invoice').should('have.value', 'York invoice');

      cy.get(`[data-cy="percentage"]`).type('42372').should('have.value', '42372');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        beer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', beerPageUrlPattern);
    });
  });
});
