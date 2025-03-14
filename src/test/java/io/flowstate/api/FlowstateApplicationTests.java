package io.flowstate.api;

import io.flowstate.api.config.PostgresContainerConfig;
import io.flowstate.api.config.RedisContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("tests")
@Import({PostgresContainerConfig.class, RedisContainerConfig.class})
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}
