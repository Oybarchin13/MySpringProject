package uz.pdp.foodswift.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurer {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfigurer(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.userDetailsService(userDetailsService);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/auth/login",
                        "/auth/register",
                        "/css/**",
                        "/js/**",
                        "/"
                ).permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")
                .anyRequest().fullyAuthenticated()
        );

        http.formLogin(form -> form
                .loginPage("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
        );

        http.rememberMe(rememberMe -> rememberMe
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("rem-me")
                .tokenValiditySeconds(60)
                .key("secret_key")
                .alwaysRemember(true)
                .userDetailsService(userDetailsService)
        );

        return http.build();
    }
}