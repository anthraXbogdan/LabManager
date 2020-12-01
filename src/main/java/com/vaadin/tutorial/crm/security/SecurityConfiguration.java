package com.vaadin.tutorial.crm.security;

import com.vaadin.tutorial.crm.backend.services.userService.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private static final String REGISTER_USER_URL = "/register";

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    /*
    @Autowired
    private DataSource dataSource;

    private final String USERS_QUERY = "select username, password, active from users where username=?";
    private final String ROLES_QUERY = "select u.username, r.role from users u inner join user_role ur on " +
            "(u.id = ur.user_id) inner join role r on (ur.role_id=r.role_id) where u.username=?";


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(ROLES_QUERY)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }
    */

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomRequestCache requestCache() {
        return new CustomRequestCache();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .requestCache().requestCache(new CustomRequestCache())
        .and()
        .authorizeRequests()
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
        .antMatchers(REGISTER_USER_URL).permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage(LOGIN_URL).permitAll()
        .successForwardUrl("/dashboard")
        .loginProcessingUrl(LOGIN_PROCESSING_URL)
        .failureUrl(LOGIN_FAILURE_URL)
        .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    /*
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // typical logged in user with some privileges
        UserDetails normalUser =
                User.withUsername("user")
                        .password("$2a$10$0O.C1D0D.g99HkDWIiBHduXLEOsCmrXCAecRkHAR77lieI2udXKta")
                        .roles("User")
                        .build();

        // admin user with all privileges
        UserDetails adminUser =
                User.withUsername("admin")
                        .password("$2a$10$0O.C1D0D.g99HkDWIiBHduXLEOsCmrXCAecRkHAR77lieI2udXKta")
                        .roles("User", "Admin")
                        .build();

        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }
    */

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**");
    }

}