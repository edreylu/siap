package com.siap.siap.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    //Configuracion de las rutas de acceso segun tu nivel de permisos en rol 

    @Override
    protected void configure(HttpSecurity httpSecurity)
            throws Exception {
        String[] resources = new String[]{"/css/**", "/js/**", "/img/**"};
        httpSecurity.authorizeRequests()
                .antMatchers(resources).permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/usuario/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/menu")
                .failureUrl("/login?error=true")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll().and().exceptionHandling().accessDeniedPage("/403");
    }

    //la autenticacion es por username y se codifica el password con PasswordEncoder el campo enabled lo solicita SpringSecurity
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,"
                        + "password,"
                        + "enabled "
                        + "from users "
                        + "where username = ?")
                .authoritiesByUsernameQuery(
                        "SELECT us.username, ro.role "
                        + " FROM \n"
                        + "user_roles ur, \n"
                        + "users us, \n"
                        + "roles ro\n"
                        + "where ur.id_user = us.id\n"
                        + "and ur.id_role = ro.id\n"
                        + "and us.username = ? ").passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
