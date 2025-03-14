package io.flowstate.api.service;

import io.flowstate.api.config.OtpConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static io.flowstate.api.util.OtpUtil.generateOtp;

@RequiredArgsConstructor
public class OtpService {

    private final OtpConfig.OtpConfigProperties configProperties;

    private final RedisTemplate<String, String> redisTemplate;

    private final PasswordEncoder passwordEncoder;

    public String generateAndStoreOtp(final UUID id) {
        final var otp = generateOtp(configProperties.length());
        final var cacheKey = getCacheKey(id);

        redisTemplate.opsForValue().set(cacheKey, passwordEncoder.encode(otp), configProperties.ttl());

        return otp;
    }

    public boolean isOtpValid(final UUID id, final String otp) {
        final var cacheKey = getCacheKey(id);

        return passwordEncoder.matches(otp, redisTemplate.opsForValue().get(cacheKey));
    }

    public void deleteOtp(final UUID id) {
        final var cacheKey = getCacheKey(id);

        redisTemplate.delete(cacheKey);
    }

    private String getCacheKey(UUID id) {
        return configProperties.cachePrefix().formatted(id);
    }
}
