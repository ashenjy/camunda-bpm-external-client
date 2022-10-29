package com.cn.camunda.auth.jwt.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

//    public static final long JWT_TOKEN_VALIDITY = 60; //1 minute
    //5 * 60 * 60 -> 5 minutes

    @Value("${application.jwt.secret-path}")
    private String jwtSecretPath;

    @Value("${application.jwt.expiration-ms}")
    private int jwtExpirationMs;

    @Value("${application.jwt.cookie-name}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public String parseJwtFromCookie(HttpServletRequest request) {
        return getJwtFromCookies(request);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser().setSigningKey(extractJwtSecret(jwtSecretPath)).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String authToken) {
        try {
            Jwts.parser().setSigningKey(extractJwtSecret(jwtSecretPath)).parseClaimsJws(authToken);
            return false;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return true;
    }

    //TODO: Auto extract secret at app startup
    private String extractJwtSecret(String jwtSecretPath) {
        String jwtSecret = null;
        if (jwtSecretPath != null) {
            try {
                InputStream inStream = Files.newInputStream(Paths.get(jwtSecretPath));
                jwtSecret = IOUtils.toString(inStream, StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("ERROR: Unable to load JWT Secret: " + e.getLocalizedMessage());
            }
        }
        return jwtSecret;
    }
}
