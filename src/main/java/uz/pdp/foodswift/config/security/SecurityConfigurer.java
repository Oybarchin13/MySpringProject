package uz.pdp.foodswift.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfigurer(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(auth -> auth.disable());
        http.userDetailsService(userDetailsService);
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register"
                        ).permitAll()
                .requestMatchers(
                        "/",
                        "/css/**"
                ).permitAll()
                .requestMatchers("/users").hasRole("ADMIN")
                .requestMatchers("/").hasRole("FOYDALANUVCHI")
                .anyRequest()
//                .authenticated()
                .fullyAuthenticated()
        );

        http.formLogin(auth -> auth
                .loginPage("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
        );

        http.rememberMe(auth -> auth
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("rem-me")
                .tokenValiditySeconds(60*60)
                .key("secret_key")
                .alwaysRemember(true)
                .userDetailsService(userDetailsService)
        );


        return http.build();
    }










}
