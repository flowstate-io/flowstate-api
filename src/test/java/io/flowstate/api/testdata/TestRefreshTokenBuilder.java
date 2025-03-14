package io.flowstate.api.testdata;

import io.flowstate.api.entity.RefreshToken;

import java.time.Instant;
import java.util.UUID;

import static io.flowstate.api.testdata.TestUserBuilder.userBuilder;
import static java.time.Duration.ofDays;

public class TestRefreshTokenBuilder {

    private final RefreshToken refreshToken;

    private TestRefreshTokenBuilder() {
        refreshToken = new RefreshToken();
        refreshToken.setExpiresAt(Instant.now().plus(ofDays(1)));
    }

    public static TestRefreshTokenBuilder refreshTokenBuilder() {
        return new TestRefreshTokenBuilder();
    }

    public TestRefreshTokenBuilder withRandomId() {
        refreshToken.setId(UUID.randomUUID());
        return this;
    }

    public TestRefreshTokenBuilder withTestUser() {
        refreshToken.setUser(userBuilder().build());
        return this;
    }

    public RefreshToken build() {
        return refreshToken;
    }

}
