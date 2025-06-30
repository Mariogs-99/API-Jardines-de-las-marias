package com.hotelJB.hotelJB_API.security;

import com.hotelJB.hotelJB_API.models.entities.User_;
import com.hotelJB.hotelJB_API.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration implements WebMvcConfigurer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenFilter filter;

    @Bean
    AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        managerBuilder
                .userDetailsService(identifier -> {
                    User_ user = userService.findByUsername(identifier);
                    if (user == null)
                        throw new UsernameNotFoundException("User: " + identifier + ", not found!");
                    return user;
                })
                .passwordEncoder(passwordEncoder);

        return managerBuilder.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Para desarrollo local (ajusta la ruta si es necesario)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");

        registry.addResourceHandler("/menu/**")
                .addResourceLocations("file:./menu/");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "https://jardindelasmarias.com",
                "https://admin.jardindelasmarias.com"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults()).csrf(csrf -> csrf.disable());

        http.cors(withDefaults()).authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll(); // ← LÍNEA CLAVE PARA CORS!

            // Público
            auth.requestMatchers("/uploads/**").permitAll();
            auth.requestMatchers("/menu/**").permitAll();
            auth.requestMatchers("/api/auth/**").permitAll();
            auth.requestMatchers("/api/paypal/**").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/api/reservation/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/api/contact-message/send").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/api/reservations/wompi/link").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/reservations/temp-reservations/**").permitAll();
            auth.requestMatchers("/api/paypal/**").permitAll();
            auth.requestMatchers("/ws-reservations/**").permitAll();
            auth.requestMatchers("/webhook-wompi").permitAll();

            // Solo ADMIN puede acceder a la gestión de usuarios
            auth.requestMatchers("/api/users/**").hasRole("ADMIN");

            // Todo lo demás requiere autenticación
            auth.anyRequest().authenticated();
        });

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(handling -> handling.authenticationEntryPoint((req, res, ex) -> {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Auth fail!");
        }));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
