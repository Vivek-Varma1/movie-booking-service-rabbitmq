package com.vivekvarma1.moviebooking.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public void addAccessTokenCookie(
            HttpServletResponse response,
            String token
    ) {
        ResponseCookie cookie = ResponseCookie.from(CookieConstants.ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(true)         // Mandatory when SameSite=None
                .sameSite("None")     // Allows cross-site cookie transmission
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearAccessTokenCookie(
            HttpServletResponse response
    ) {
        ResponseCookie cookie = ResponseCookie.from(CookieConstants.ACCESS_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (CookieConstants.ACCESS_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}