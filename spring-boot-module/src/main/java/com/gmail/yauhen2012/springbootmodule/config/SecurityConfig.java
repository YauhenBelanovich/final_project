package com.gmail.yauhen2012.springbootmodule.config;

import com.gmail.yauhen2012.repository.model.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@Order(2)
@Profile("!test")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/users/**")
                .hasRole(RoleEnum.ADMINISTRATOR.name())
                .antMatchers("/reviews", "/reviews/**/delete", "/reviews/**/newStatus")
                .hasRole(RoleEnum.ADMINISTRATOR.name())
                .antMatchers("/reviews/new")
                .hasRole(RoleEnum.CUSTOMER_USER.name())
                .antMatchers("/items/**/delete", "/items/**/copy")
                .hasRole(RoleEnum.SALE_USER.name())
                .antMatchers("/items/**/")
                .hasAnyRole(RoleEnum.SALE_USER.name(),
                        RoleEnum.CUSTOMER_USER.name())
                .antMatchers("/articles/new", "/articles/edit", "/articles/**/delete", "/articles/comment/**")
                .hasRole(RoleEnum.SALE_USER.name())
                .antMatchers("/articles/**/")
                .hasAnyRole(RoleEnum.SALE_USER.name(),
                        RoleEnum.CUSTOMER_USER.name())
                .antMatchers("/info/**")
                .hasAnyRole(RoleEnum.SALE_USER.name(),
                        RoleEnum.ADMINISTRATOR.name(),
                        RoleEnum.CUSTOMER_USER.name())
                .antMatchers("/profile/**")
                .hasAnyRole(
                        RoleEnum.SALE_USER.name(),
                        RoleEnum.ADMINISTRATOR.name(),
                        RoleEnum.CUSTOMER_USER.name())
                .antMatchers("/orders")
                .hasAnyRole(
                        RoleEnum.CUSTOMER_USER.name(),
                        RoleEnum.SALE_USER.name())
                .antMatchers("/orders/new", "/orders/cart", "/orders/cart/**")
                .hasRole(RoleEnum.CUSTOMER_USER.name())
                .antMatchers("/orders/**/", "/orders/**/newStatus")
                .hasRole(RoleEnum.SALE_USER.name())
                .and()
                .formLogin().loginPage("/login")
                .successHandler(myAuthenticationSuccessHandler())
                .permitAll()
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf().disable();
    }

}
