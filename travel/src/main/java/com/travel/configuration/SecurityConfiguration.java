package com.travel.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource datasource;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
          .jdbcAuthentication()
          .dataSource(this.datasource)
          .usersByUsernameQuery("SELECT username,password,enabled " + "FROM users " + "WHERE username = ?")
          .authoritiesByUsernameQuery("SELECT username,authority " + "FROM authorities " + "WHERE username = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .authorizeRequests()
          .antMatchers("/api/v1/expenses/**")
          .hasAuthority("ROLE_OFFICE_MANAGER")
          .antMatchers("/api/v1/warrants/**")
          .hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_OFFICE_MANAGER")
          .anyRequest()
          .authenticated()
          .and()
          .formLogin()
          .and()
          .httpBasic();

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
