package com.lab900.tunch.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.lab900.tunch.config.ApplicationProperties;
import com.lab900.tunch.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(excludeAutoConfiguration = { LiquibaseAutoConfiguration.class })
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
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
