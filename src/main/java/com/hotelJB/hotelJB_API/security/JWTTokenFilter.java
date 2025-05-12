package com.hotelJB.hotelJB_API.security;

import com.hotelJB.hotelJB_API.models.entities.User_;
import com.hotelJB.hotelJB_API.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    @Autowired
    JWTTools jwtTools;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        System.out.println("Solicitud recibida en: " + request.getRequestURI());

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            System.out.println("Token recibido: " + token);

            try {
                username = jwtTools.getUsernameFrom(token);
                System.out.println("Usuario extraído del token: " + username);
            } catch (IllegalArgumentException e) {
                System.out.println("No se pudo obtener el token JWT");
            } catch (ExpiredJwtException e) {
                System.out.println("El token JWT ha expirado");
            } catch (MalformedJwtException e) {
                System.out.println("El token JWT es malformado");
            }
        } else {
            System.out.println("No se encontró la cabecera Authorization");
        }

        if (username != null && token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User_ user = userService.findByUsername(username);

            if (user != null) {
                Boolean tokenValidity = userService.isTokenValid(user, token);
                System.out.println("Validación del token para " + username + ": " + tokenValidity);

                if (tokenValidity) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("Usuario autenticado: " + username);
                } else {
                    System.out.println("Token inválido para " + username);
                }
            } else {
                System.out.println("Usuario no encontrado en la base de datos");
            }
        }

        filterChain.doFilter(request, response);
    }

}
