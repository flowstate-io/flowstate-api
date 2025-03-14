package io.flowstate.api.repository;

import io.flowstate.api.config.JpaConfig;
import io.flowstate.api.config.PostgresContainerConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles("tests")
@AutoConfigureTestDatabase(replace = NONE)
@Import({JpaConfig.class, PostgresContainerConfig.class})
public abstract class JpaTest {

}
