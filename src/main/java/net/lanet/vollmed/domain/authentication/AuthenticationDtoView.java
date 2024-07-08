package net.lanet.vollmed.domain.authentication;

public record AuthenticationDtoView(
        String token,
        String refreshToken
) {
}
