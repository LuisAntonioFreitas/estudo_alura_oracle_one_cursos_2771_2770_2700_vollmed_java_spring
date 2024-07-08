package net.lanet.vollmed.infra.security;

import net.lanet.vollmed.infra.exception.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    private SecurityFilter securityFilter;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
//                // Desabilitar a segurança HTTP
//                http.csrf(csrf -> csrf.disable())
//                        .authorizeHttpRequests(req -> {
//                            req.anyRequest().permitAll();
//                        }).build();

                http.csrf(csrf -> csrf.disable())
                        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(req -> {
                            // GET
                            req.requestMatchers(HttpMethod.GET,"/").permitAll();
                            req.requestMatchers(HttpMethod.GET,"/status").permitAll();
                            req.requestMatchers(HttpMethod.GET,"/error", "/error/**").permitAll();
                            req.requestMatchers(HttpMethod.GET,"/doc", "/doc/**").permitAll();
                            req.requestMatchers(HttpMethod.GET,"/v3", "/v3/**", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui", "/swagger-ui/**").permitAll();
                            // POST
                            req.requestMatchers(HttpMethod.POST,"/login").permitAll();
                            req.anyRequest().authenticated();
                        })
                        .exceptionHandling(ex -> {
                            ex.authenticationEntryPoint(customAuthenticationEntryPoint);
                        })
                        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/resources/**", "/static/**", "/images/**", "/css/**", "/js/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
