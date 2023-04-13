package com.lab900.tunch.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.lab900.tunch.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("testdev")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findOneByEmailIgnoreCase() {
        var user = new User();
        user.setFirstName("Koen");
        user.setLastName("De Vos");
        user.setEmail("koen.daelman@lab900.com");

        entityManager.persist(user);
        entityManager.flush();

        var oneByEmailIgnoreCase = userRepository.findOneByEmailIgnoreCase(user.getEmail().toUpperCase());
        assertThat(oneByEmailIgnoreCase.isPresent(), equalTo(true));
    }
}
