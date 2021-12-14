package ru.vsu.cs.proskuryakov.coffeestrike.security;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.vsu.cs.proskuryakov.coffeestrike.security.converter.AuthenticationConverterImpl;
import ru.vsu.cs.proskuryakov.coffeestrike.security.handler.AuthenticationSuccessHandlerImpl;
import ru.vsu.cs.proskuryakov.coffeestrike.security.handler.LogoutSuccessHandlerImpl;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final MapperFacade mapperFacade;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder("", 141_248, 512);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        builder.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final var authenticationFilter = new AuthenticationFilter(
                authenticationManager(),
                new AuthenticationConverterImpl()
        );

        authenticationFilter.setRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

        authenticationFilter.setSuccessHandler(new AuthenticationSuccessHandlerImpl(userRepository, mapperFacade));

        authenticationFilter.setFailureHandler(new AuthenticationFailureHandler() {
            private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

            @Override
            public void onAuthenticationFailure(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    AuthenticationException exception
            ) {
                logger.error("Authentication error", exception);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        });


        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/product").permitAll()
                .antMatchers("/api/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandlerImpl())
                .logoutUrl("/api/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .addFilterAfter(authenticationFilter, LogoutFilter.class)
        ;

    }

}
