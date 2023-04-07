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

describe('BeerBottle e2e test', () => {
  const beerBottlePageUrl = '/beer-bottle';
  const beerBottlePageUrlPattern = new RegExp('/beer-bottle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const beerBottleSample = { expirationDate: '2023-04-07' };

  let beerBottle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/beer-bottles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/beer-bottles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/beer-bottles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (beerBottle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/beer-bottles/${beerBottle.id}`,
      }).then(() => {
        beerBottle = undefined;
      });
    }
  });

  it('BeerBottles menu should load BeerBottles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('beer-bottle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BeerBottle').should('exist');
    cy.url().should('match', beerBottlePageUrlPattern);
  });

  describe('BeerBottle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(beerBottlePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BeerBottle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/beer-bottle/new$'));
        cy.getEntityCreateUpdateHeading('BeerBottle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerBottlePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/beer-bottles',
          body: beerBottleSample,
        }).then(({ body }) => {
          beerBottle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/beer-bottles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/beer-bottles?page=0&size=20>; rel="last",<http://localhost/api/beer-bottles?page=0&size=20>; rel="first"',
              },
              body: [beerBottle],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(beerBottlePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BeerBottle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('beerBottle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerBottlePageUrlPattern);
      });

      it('edit button click should load edit BeerBottle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BeerBottle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerBottlePageUrlPattern);
      });

      it('edit button click should load edit BeerBottle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BeerBottle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerBottlePageUrlPattern);
      });

      it('last delete button click should delete instance of BeerBottle', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('beerBottle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beerBottlePageUrlPattern);

        beerBottle = undefined;
      });
    });
  });

  describe('new BeerBottle page', () => {
    beforeEach(() => {
      cy.visit(`${beerBottlePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BeerBottle');
    });

    it('should create an instance of BeerBottle', () => {
      cy.get(`[data-cy="expirationDate"]`).type('2023-04-07').blur().should('have.value', '2023-04-07');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        beerBottle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', beerBottlePageUrlPattern);
    });
  });
});
