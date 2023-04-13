package com.lab900.tunch;

import com.lab900.tunch.domain.AreYouCrazyException;
import com.lab900.tunch.domain.Beer;
import com.lab900.tunch.domain.BeerBottle;
import com.lab900.tunch.domain.Fridge;
import com.lab900.tunch.repository.FridgeRepository;
import com.lab900.tunch.service.FridgeService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@ContextConfiguration(classes = TunchApp.class)
public class FridgeServiceTestDirties {

    @MockBean
    private FridgeRepository fridgeRepository;

    @Autowired
    private FridgeService fridgeService;

    @Test
    public void testGetFridgeById() {
        Fridge Fridge = new Fridge();
        Fridge.setName("IPA");

        Mockito.when(fridgeRepository.findById(1L)).thenReturn(Optional.of(Fridge));
        var fridgeDTO = fridgeService.findOne(1L);

        Assertions.assertThat(fridgeDTO.get().getName()).isEqualTo("IPA");
    }


    @Test
    public void testHowLongToCoolThrows() {
        var beer = new Beer();
        var beerBottle = new BeerBottle(beer);
        Assertions.assertThatThrownBy(() -> {
            fridgeService.howTongToCoolUntilReached(beerBottle, new BigDecimal("-1"));
        }).isInstanceOf(AreYouCrazyException.class);
    }
}
