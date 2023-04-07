package com.lab900.tunch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lab900.tunch.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BeerBottleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeerBottle.class);
        BeerBottle beerBottle1 = new BeerBottle();
        beerBottle1.setId(UUID.randomUUID());
        BeerBottle beerBottle2 = new BeerBottle();
        beerBottle2.setId(beerBottle1.getId());
        assertThat(beerBottle1).isEqualTo(beerBottle2);
        beerBottle2.setId(UUID.randomUUID());
        assertThat(beerBottle1).isNotEqualTo(beerBottle2);
        beerBottle1.setId(null);
        assertThat(beerBottle1).isNotEqualTo(beerBottle2);
    }
}
