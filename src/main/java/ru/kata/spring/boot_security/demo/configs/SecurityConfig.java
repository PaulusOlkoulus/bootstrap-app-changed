package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final SuccessUserHandler successUserHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/user/**", "/logout").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin().successHandler(successUserHandler).permitAll()
                .and()
                .logout().logoutSuccessUrl("/login")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));;

    }


    //IN-MEMORY
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService(){
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$12$VTvb8zOPSphE0hjhQ3onbOoo1gVCYRMCdcUijhPm2luabjFy3HTPa")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$VTvb8zOPSphE0hjhQ3onbOoo1gVCYRMCdcUijhPm2luabjFy3HTPa")
//                .roles("ADMIN","USER")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    // JDBC auth

//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource){
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$12$VTvb8zOPSphE0hjhQ3onbOoo1gVCYRMCdcUijhPm2luabjFy3HTPa")//100
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$VTvb8zOPSphE0hjhQ3onbOoo1gVCYRMCdcUijhPm2luabjFy3HTPa")//100
//                .roles("ADMIN","USER")
//                .build();
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        if(!jdbcUserDetailsManager.userExists(user.getUsername())) {
//            jdbcUserDetailsManager.createUser(user);
//        }
//        if(!jdbcUserDetailsManager.userExists(admin.getUsername())) {
//            jdbcUserDetailsManager.createUser(admin);
//        }
//
//        return jdbcUserDetailsManager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }
}
