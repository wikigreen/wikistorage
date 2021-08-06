package com.wikigreen.wikistorage.security;

import com.wikigreen.wikistorage.security.jwt.JwtConfigurer;
import com.wikigreen.wikistorage.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider tokenProvider;

    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String MODERATOR_ENDPOINT = "/api/v1/moderator/**";
    private static final String USER_ENDPOINT = "/api/v1/profile/**";
    private static final String AUTH_ENDPOINT = "/api/v1/auth/**";

    public ApplicationSecurityConfig(JwtTokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureSafe(http);
    }

    protected void configureSafe(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasRole("USER")
                .antMatchers(MODERATOR_ENDPOINT).hasRole("MODERATOR")
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(tokenProvider));
    }

    protected void configureUnsafe(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();
    }
}
