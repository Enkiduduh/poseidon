package com.app.poseidon.security;

import com.app.poseidon.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Représente la configuration de sécurité de l'application.
 *
 * <p>
 * Cette entité contient la gestion de l'authentification d'un user et la chaîne de filtre de sécurité
 * pour l'accès aux pages de l'application.
 * </p>
 *
 * @author Ilyes Soumar Djouma
 * @version 1.0
 * @since 2025-06-27
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * Instance de UserService
     */
    private final UserService userService;

    /**
     * Instance de PasswordEncoder
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Initialisation de l'objet SecurityConfig
     */
    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retourne un objet authProvider qui permet de confirmer l'authentification d'un utilisateur.
     *
     * @return l'authProvider de l'utilisateur
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Représente la chaîne de filtre de sécurité qui va gérer toute la sécurité de l'application.
     * <p>
     * Cette méthode:
     * <ol>
     *      <li>Enregistre le provider d'authentification</li>
     *      <li>Définit la politique de session</li>
     *      <li>Détermine la gestion d'accès aux pages de l'app (RBAC)</li>
     *      <li>Configure le login et son URL de traitement</li>
     *      <li>Configure le logout</li>
     * </ol>
     * </p>
     *
     * @param http         la configuration HTTP en cours
     * @param authProvider le provider Spring Security chargé de valider les credentials
     * @return la chaîne de filtre de sécurité complétement configurée
     * @throws Exception en cas d'erreur de configuration du builder
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {
        http
                .authenticationProvider(authProvider)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/css/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(
                                "/bidList/**",
                                "/trade/**",
                                "/curvePoint/**",
                                "/rating/**",
                                "/ruleName/**"
                        ).hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/**").hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/do-login") // POST pour traiter la soumission
                        .defaultSuccessUrl("/bidList/list", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/app-logout", "POST"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }
}
