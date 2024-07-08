package net.lanet.vollmed.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import net.lanet.vollmed.domain.usuario.Usuario;
import net.lanet.vollmed.infra.utilities.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.*;

@Service
public class TokenService {
    @Autowired
    ApplicationProperties ap;
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Object id, String item, String typeToken) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ap.apiSystemReference)
                    .withSubject(item)
                    .withClaim("id", String.valueOf(id))
                    .withExpiresAt(dateExpiration(typeToken))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token.", exception);
        }
    }

    public Map<String, Object> validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ap.apiSystemReference)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            Map<String, Object> map = new HashMap<>();
            map.put("subject", jwt.getSubject());
            map.put("id", jwt.getClaim("id").asString());
            return map;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token inválido ou expirado.", exception);
        }
    }

    private Instant dateExpiration(String typeToken) {
        Instant dateExpiration = dateExpirationToken();
        if (typeToken != null) {
            if (typeToken.equalsIgnoreCase("refresh")) {
                dateExpiration = dateExpirationRefreshToken();
            }
        }
        return dateExpiration;
    }
    private Instant dateExpirationToken() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of(getOffset()));
    }
    private Instant dateExpirationRefreshToken() {
        return LocalDateTime.now()
                .plusWeeks(1)
                .toInstant(ZoneOffset.of(getOffset()));
    }

    private String getOffset() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Locale locale = request.getLocale();
        Calendar calenda = Calendar.getInstance(locale);
        TimeZone timeZone = calenda.getTimeZone();
        int rawOffset = timeZone.getRawOffset();
        ZoneId zoneId = timeZone.toZoneId();
        ZoneRules rules = zoneId.getRules();

        return String.valueOf(zoneId.getRules().getOffset(LocalDateTime.now()));
    }

    private void showLocale() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Locale locale = request.getLocale();
        Calendar calenda = Calendar.getInstance(locale);
        TimeZone timeZone = calenda.getTimeZone();
        int rawOffset = timeZone.getRawOffset();
        ZoneId zoneId = timeZone.toZoneId();
        ZoneRules rules = zoneId.getRules();

        System.out.println("locale: " + locale);
        System.out.println("calenda: " + calenda);
        System.out.println("timeZone: " + timeZone);
        System.out.println("rawOffset: " + rawOffset);
        System.out.println("zoneId: " + zoneId);
        System.out.println("rules: " + rules);
    }

}
