package io.flowstate.api.testdata;

import io.flowstate.api.entity.User;

import java.util.UUID;

public class TestUserBuilder {

    private final User user;

    private TestUserBuilder() {
        user = new User();
        user.setUsername("testUser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmailVerified(false);
    }

    public static TestUserBuilder userBuilder() {
        return new TestUserBuilder();
    }

    public TestUserBuilder withRandomId() {
        user.setId(UUID.randomUUID());
        return this;
    }

    public TestUserBuilder withVerifiedEmail() {
        user.setEmailVerified(true);
        return this;
    }

    public User build() {
        return user;
    }

}
