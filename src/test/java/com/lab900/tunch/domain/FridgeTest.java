package com.lab900.tunch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lab900.tunch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FridgeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fridge.class);
        Fridge fridge1 = new Fridge();
        fridge1.setId(1L);
        Fridge fridge2 = new Fridge();
        fridge2.setId(fridge1.getId());
        assertThat(fridge1).isEqualTo(fridge2);
        fridge2.setId(2L);
        assertThat(fridge1).isNotEqualTo(fridge2);
        fridge1.setId(null);
        assertThat(fridge1).isNotEqualTo(fridge2);
    }
}
