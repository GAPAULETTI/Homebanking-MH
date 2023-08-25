package com.mindhub.homebanking.configurations;


import com.mindhub.homebanking.models.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http.authorizeRequests()

                .antMatchers("/manager.html", "/rest/**").hasAuthority("ADMIN")
                .antMatchers("h2-console").hasAuthority("ADMIN")
                .antMatchers( "/api/login", "/api/logout").permitAll()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers("/api/clients/accounts/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers( "api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers( "/api/**").hasAuthority("ADMIN");


        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout");

        http.csrf().disable();
        http.headers().disable();
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.formLogin().successHandler((request, response, authentication) -> clearAuthenticationAttributes(request));
        http.formLogin().failureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
