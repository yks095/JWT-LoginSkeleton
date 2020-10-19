package me.kiseok.jwtskeleton.config;

import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.config.jwt.JwtAuthenticationFilter;
import me.kiseok.jwtskeleton.config.jwt.JwtProvider;
import me.kiseok.jwtskeleton.domain.token.TokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    private static final String[] AUTH_WHITELIST = {
            // — swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/login/google/auth", "/sign-in", "/api/accounts", "/api/login", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenRepository, jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        ;
    }
}
