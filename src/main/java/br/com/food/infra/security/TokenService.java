package br.com.food.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // A chave de rotação pode ser alterada periodicamente para invalidar tokens antigos
    @Value("${api.security.token.rotation-secret}")
    private String rotationSecret;

    public String generateToken(ResourceOwner user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("food-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(authority -> "ROLE_" + authority.getAuthority())
                            .collect(Collectors.toList()))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(rotationSecret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("food-api")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        Date expiresAt = jwt.getClaim("exp").asDate();
        if (expiresAt.before(new Date())) {
            throw new JWTVerificationException("The token has expired");
        }
        return jwt.getSubject();
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    // Método para gerar um novo token usando a chave de rotação
    public String generateTokenWithRotationKey(ResourceOwner user) {
        try {
            Algorithm rotationAlgorithm = Algorithm.HMAC256(rotationSecret);
            String token = JWT.create()
                    .withIssuer("food-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .withClaim("idUser", user.getId())
                    .withClaim("idestabelecimento", user.getUsuario().getEstabelecimento().getIdestabelecimento())
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(authority -> "ROLE_" + authority.getAuthority())
                            .collect(Collectors.toList()))
                    .sign(rotationAlgorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token with rotation key", exception);
        }
    }

    public String generateRefreshToken(ResourceOwner user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(rotationSecret);
            String token = JWT.create()
                    .withIssuer("food-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genRefreshTokenExpirationDate())
                    .withClaim("idUser", user.getId())
                    .withClaim("idestabelecimento", user.getUsuario().getEstabelecimento().getIdestabelecimento())
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(authority -> "ROLE_" + authority.getAuthority())
                            .collect(Collectors.toList()))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating refresh token", exception);
        }
    }

    private Instant genRefreshTokenExpirationDate() {
        return LocalDateTime.now().plusDays(365).toInstant(ZoneOffset.of("-03:00"));
    }
}
