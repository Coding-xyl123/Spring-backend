package com.luv2code.springboot.thymeleafdemo.security;

import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {
    //Bcrypt bean definition
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationProvider bean definition
    @Bean
    public DaoAuthenticationProvider authenticationProvider(EmployeeService employeeService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(employeeService);//set custom user detail service
        auth.setPasswordEncoder(passwordEncoder());// set the password encoder - bcrypt
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").hasRole("EMPLOYEE")
                                .requestMatchers("/leaders/**").hasRole("MANAGER")
                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .requestMatchers("/register/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );

        return http.build();
    }


    //   @Bean
//    public InMemoryUserDetailsManager userDetailsManager(){
//        UserDetails Nicholas_Bear_Brown = User.builder()
//                .username("Nicholas")
//                .password("{noop}test123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails Gerardo_Serrano = User.builder()
//                .username("Gerardo")
//                .password("{noop}test123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails Nina_Harris = User.builder()
//                .username("Nina")
//                .password("{noop}test123")
//                .roles("ADMIN", "MANAGER")
//                .build();
//
//        UserDetails Gayathri_Vummenthala = User.builder()
//                .username("Gayathri")
//                .password("{noop}test123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails Himanshi_Motwani = User.builder()
//                .username("Himanshi")
//                .password("{noop}test123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails Megha_Patel = User.builder()
//                .username("Megha")
//                .password("{noop}test123")
//                .roles("MANAGER")
//                .build();
//
//        UserDetails Raghnya_Valluru = User.builder()
//                .username("Raghnya")
//                .password("{noop}test123")
//                .roles("MANAGER")
//                .build();
//
//        UserDetails Xiangyang_Liu = User.builder()
//                .username("Xiangyang")
//                .password("{noop}test123")
//                .roles("ADMIN", "MANAGER", "EMPLOYEE")
//                .build();
//
//        UserDetails Olivia = User.builder()
//                .username("Olivia")
//                .password("{noop}test123")
//                .roles("EMPLOYEE")
//                .build();
//
//        return new InMemoryUserDetailsManager(Nicholas_Bear_Brown, Gerardo_Serrano,Nina_Harris,Gayathri_Vummenthala,Himanshi_Motwani, Megha_Patel, Raghnya_Valluru, Xiangyang_Liu,Olivia);
//    }

    //add support for JDBC and no more hard code
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        //Tell Spring Security to use JDBC authentication with data source
        // JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        //define query to retrieve a user by username
        // jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id =?");
        //define query to retrieve the authoritities/role by user name
        // jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
        //        "select user_id, role from roles where user_id = ?"
        // );
        return new JdbcUserDetailsManager(dataSource);

    }
}
//    @Bean
//    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception{
//        http.authorizeHttpRequests(configurer ->
//                configurer
//                        .requestMatchers("/").hasRole("EMPLOYEE")
//                        .requestMatchers("/leaders/**").hasRole("MANAGER")
//                        .requestMatchers("/systems/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form ->
//                        form
//                                .loginPage("/showMyLoginPage")
//                                .loginProcessingUrl("/authenticateTheUser")
//                                .permitAll()
//                )
//                .logout(logout ->logout.permitAll()
//                )
//                .exceptionHandling(configurer ->
//                        configurer.accessDeniedPage("/access-denied"));
//
//
//        return http.build();
//    }
//
//}
