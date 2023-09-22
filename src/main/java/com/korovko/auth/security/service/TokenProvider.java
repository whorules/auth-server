package com.korovko.auth.security.service;

import com.korovko.auth.config.ApplicationSecurityProperties;
import com.korovko.auth.security.dto.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    public static final String SUBJECT = "Me";
    public static final String USERNAME = "username";
    public static final String ROLES = "roles";
    public static final String ACCOUNT_EXPIRED = "accountExpired";
    public static final String ACCOUNT_LOCKED = "accountLocked";
    public static final String CREDENTIALS_EXPIRED = "credentialsExpired";
    public static final String ENABLED = "enabled";
    public static final String USER_ID = "userId";

    private final ApplicationSecurityProperties applicationSecurityProperties;

    public UserPrincipal getPrincipalFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(applicationSecurityProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            UserPrincipal userPrincipal = new UserPrincipal()
                    .setUsername((String) body.get(USERNAME))
                    .setAuthorities(((List<String>) body.get(ROLES)).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet()))
                    .setAccountExpired((boolean) body.get(ACCOUNT_EXPIRED))
                    .setAccountLocked((boolean) body.get(ACCOUNT_LOCKED))
                    .setCredentialsExpired((boolean) body.get(CREDENTIALS_EXPIRED))
                    .setEnabled((boolean) body.get(ENABLED));
            userPrincipal.setUserId(UUID.fromString((String) body.get(USER_ID)));
            return userPrincipal;
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Token expired");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Invalid token");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Invalid token data");
        }
    }

    public String buildToken(String username, UserPrincipal principal) {
        return Jwts.builder()
                .setSubject(SUBJECT)
                .signWith(Keys.hmacShaKeyFor(applicationSecurityProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(applicationSecurityProperties.getJwtTimeToLive())))
                .claim(USER_ID, principal.getUserId().toString())
                .claim(USERNAME, username)
                .claim(ROLES, principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .claim(ACCOUNT_EXPIRED, principal.isAccountExpired())
                .claim(ACCOUNT_LOCKED, principal.isAccountLocked())
                .claim(CREDENTIALS_EXPIRED, principal.isCredentialsExpired())
                .claim(ENABLED, principal.isEnabled())
                .compact();
    }
}
