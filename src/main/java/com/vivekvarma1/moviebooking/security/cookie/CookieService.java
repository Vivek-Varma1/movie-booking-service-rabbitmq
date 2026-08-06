package com.vivekvarma1.moviebooking.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public void addAccessTokenCookie(
            HttpServletResponse response,
            String token
    ) {

        Cookie cookie = new Cookie(
                CookieConstants.ACCESS_TOKEN,
                token
        );

        cookie.setHttpOnly(true);

        // Change to true in production (HTTPS)
        cookie.setSecure(true);

        cookie.setPath("/");

        cookie.setMaxAge((int) (jwtExpiration / 1000));

        response.addCookie(cookie);
    }

    public void clearAccessTokenCookie(
            HttpServletResponse response
    ) {

        Cookie cookie = new Cookie(
                CookieConstants.ACCESS_TOKEN,
                ""
        );

        cookie.setHttpOnly(true);

        cookie.setSecure(false);

        cookie.setPath("/");

        cookie.setMaxAge(0);

        response.addCookie(cookie);
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