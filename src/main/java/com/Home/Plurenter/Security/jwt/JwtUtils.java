package com.Home.Plurenter.Security.jwt;

import java.util.Date;

import com.Home.Plurenter.Service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

//    @Value("classpath:resources/private.txt")
    private String jwtSecret ="-----BEGIN PRIVATE KEY-----\n" +
        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCWK8UjyoHgPTLa\n" +
        "PLQJ8SoXLLjpHSjtLxMqmzHnFscqhTVVaDpCRCb6e3Ii/WniQTWw8RA7vf4djz4H\n" +
        "OzvlfBFNgvUGZHXDwnmGaNVaNzpHYFMEYBhE8VGGiveSkzqeLZI+Y02G6sQAfDtN\n" +
        "qqzM/l5QX8X34oQFaTBW1r49nftvCpITiwJvWyhkWtXP9RP8sXi1im5Vi3dhupOh\n" +
        "nelk5n0BfajUYIbfHA6ORzjHRbt7NtBl0L2J+0/FUdHyKs6KMlFGNw8O0Dq88qnM\n" +
        "uXoLJiewhg9332W3DFMeOveel+//cvDnRsCRtPgd4sXFPHh+UShkso7+DRsChXa6\n" +
        "oGGQD3GdAgMBAAECggEAAjfTSZwMHwvIXIDZB+yP+pemg4ryt84iMlbofclQV8hv\n" +
        "6TsI4UGwcbKxFOM5VSYxbNOisb80qasb929gixsyBjsQ8284bhPJR7r0q8h1C+jY\n" +
        "URA6S4pk8d/LmFakXwG9Tz6YPo3pJziuh48lzkFTk0xW2Dp4SLwtAptZY/+ZXyJ6\n" +
        "96QXDrZKSSM99Jh9s7a0ST66WoxSS0UC51ak+Keb0KJ1jz4bIJ2C3r4rYlSu4hHB\n" +
        "Y73GfkWORtQuyUDa9yDOem0/z0nr6pp+pBSXPLHADsqvZiIhxD/O0Xk5I6/zVHB3\n" +
        "zuoQqLERk0WvA8FXz2o8AYwcQRY2g30eX9kU4uDQAQKBgQDmf7KGImUGitsEPepF\n" +
        "KH5yLWYWqghHx6wfV+fdbBxoqn9WlwcQ7JbynIiVx8MX8/1lLCCe8v41ypu/eLtP\n" +
        "iY1ev2IKdrUStvYRSsFigRkuPHUo1ajsGHQd+ucTDf58mn7kRLW1JGMeGxo/t32B\n" +
        "m96Af6AiPWPEJuVfgGV0iwg+HQKBgQCmyPzL9M2rhYZn1AozRUguvlpmJHU2DpqS\n" +
        "34Q+7x2Ghf7MgBUhqE0t3FAOxEC7IYBwHmeYOvFR8ZkVRKNF4gbnF9RtLdz0DMEG\n" +
        "5qsMnvJUSQbNB1yVjUCnDAtElqiFRlQ/k0LgYkjKDY7LfciZl9uJRl0OSYeX/qG2\n" +
        "tRW09tOpgQKBgBSGkpM3RN/MRayfBtmZvYjVWh3yjkI2GbHA1jj1g6IebLB9SnfL\n" +
        "WbXJErCj1U+wvoPf5hfBc7m+jRgD3Eo86YXibQyZfY5pFIh9q7Ll5CQl5hj4zc4Y\n" +
        "b16sFR+xQ1Q9Pcd+BuBWmSz5JOE/qcF869dthgkGhnfVLt/OQzqZluZRAoGAXQ09\n" +
        "nT0TkmKIvlza5Af/YbTqEpq8mlBDhTYXPlWCD4+qvMWpBII1rSSBtftgcgca9XLB\n" +
        "MXmRMbqtQeRtg4u7dishZVh1MeP7vbHsNLppUQT9Ol6lFPsd2xUpJDc6BkFat62d\n" +
        "Xjr3iWNPC9E9nhPPdCNBv7reX7q81obpeXFMXgECgYEAmk2Qlus3OV0tfoNRqNpe\n" +
        "Mb0teduf2+h3xaI1XDIzPVtZF35ELY/RkAHlmWRT4PCdR0zXDidE67L6XdJyecSt\n" +
        "FdOUH8z5qUraVVebRFvJqf/oGsXc4+ex1ZKUTbY0wqY1y9E39yvB3MaTmZFuuqk8\n" +
        "f3cg+fr8aou7pr9SHhJlZCU=\n" +
        "-----END PRIVATE KEY-----";

    @Value("${tinder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

//        Resource resource = new ClassPathResource("public.txt");
//        Resource privateResource = new ClassPathResource("private.txt");
//        String publicKey = resource.toString();
//        String privateKey = privateResource.toString();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}