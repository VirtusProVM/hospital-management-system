package com.project.hms.security;

import com.project.hms.components.RoleBasedLogoutSuccessHandler;
import com.project.hms.service.AdminDetailsService;
import com.project.hms.service.CashierDetailsService;
import com.project.hms.service.DoctorDetailsService;
import com.project.hms.service.ReceptionDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /*
    @Bean
    public DaoAuthenticationProvider adminAuthProvider(UserDetailServiceImpl adminDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider doctorAuthProvider(DoctorDetailsService doctorDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(doctorDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }*/

    /*
    @Bean
    public DaoAuthenticationProvider cashierAuthProvider(CashierDetailsService cashierDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(cashierDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }


    @Bean
    public DaoAuthenticationProvider receptionistAuthProvider(ReceptionistDetailsService receptionistDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(receptionistDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
*/

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider doctorAuthProvider,
                                                   DaoAuthenticationProvider adminAuthProvider
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/doctors/**").hasRole("DOCTOR")
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .requestMatchers("/doctor/login", "/admin/login",
                                "/doctor/login-process", "/admin/login-process").permitAll()
                        .anyRequest().authenticated()
                )
                // Doctor login
                .formLogin(form -> form
                        .loginPage("/doctor/login")
                        .loginProcessingUrl("/doctor/login-process")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/doctors", true)
                        .failureUrl("/doctors/login?error=true")
                )
                // Admin login
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login-process")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admins", true)
                        .failureUrl("/admins/login?error=true")
                )
                .authenticationProvider(doctorAuthProvider)
                .authenticationProvider(adminAuthProvider)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }*/

    /*
    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .requestMatchers("/doctors/**").hasRole("DOCTOR")
                        .requestMatchers("/receptions/**").hasRole("RECEPTION")
                        .requestMatchers("/cashiers/**").hasRole("CASHIER")
                        .requestMatchers("/pharmacies/**").hasRole("PHARMACY")
                        .requestMatchers("/labs/**").hasRole("LAB")
                        .requestMatchers("/register").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admins", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                ).sessionManagement(session -> session.maximumSessions(100).maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry()));
        return http.build();
    }*/

    @Bean
    public SecurityFilterChain doctorSecurityFilterChain(HttpSecurity http, DoctorDetailsService doctorDetailsService,
                                                         RoleBasedLogoutSuccessHandler roleBasedLogoutSuccessHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .securityMatcher("/doctor/**", "/doctor/login", "/doctor/login-process")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/doctor/login", "/doctor/login-process").permitAll()
                        .anyRequest().hasRole("DOCTOR")
                )
                .formLogin(form -> form
                        .loginPage("/doctor/login")
                        .loginProcessingUrl("/doctor/login-process")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/doctor/", true)
                        .failureUrl("/doctor/login?error")
                )
                .userDetailsService(doctorDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/doctor/logout")
                        .logoutSuccessUrl("/doctor/login?logout")
                        .logoutSuccessHandler(roleBasedLogoutSuccessHandler)
                );
        return http.build();
    }

    // Admin chain
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http, AdminDetailsService adminDetailsService,
                                                        RoleBasedLogoutSuccessHandler roleBasedLogoutSuccessHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .securityMatcher("/admin/**", "/admins/**", "/admin/login", "/admin/login-process")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login", "/admin/login-process").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login-process")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admins/", true)
                        .failureUrl("/admin/login?error")
                )
                .userDetailsService(adminDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                        .logoutSuccessHandler(roleBasedLogoutSuccessHandler)
                );
        return http.build();
    }

    //Reception chain
    @Bean
    public SecurityFilterChain receptionSecurityFilterChain(HttpSecurity http, ReceptionDetailsService receptionDetailsService,
                                                        RoleBasedLogoutSuccessHandler roleBasedLogoutSuccessHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .securityMatcher("/reception/**", "/reception/**", "/reception/login", "/reception/login-process")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/reception/login", "/reception/login-process").permitAll()
                        .anyRequest().hasRole("RECEPTION")
                )
                .formLogin(form -> form
                        .loginPage("/reception/login")
                        .loginProcessingUrl("/reception/login-process")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/reception/", true)
                        .failureUrl("/reception/login?error")
                )
                .userDetailsService(receptionDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/reception/logout")
                        .logoutSuccessUrl("/reception/login?logout")
                        .logoutSuccessHandler(roleBasedLogoutSuccessHandler)
                );
        return http.build();
    }

    @Bean
    public SecurityFilterChain cashierSecurityFilterChain(HttpSecurity http, CashierDetailsService cashierDetailsService,
                                                            RoleBasedLogoutSuccessHandler roleBasedLogoutSuccessHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .securityMatcher("/cashier/**", "/cashier/**", "/cashier/login", "/cashier/login-process")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/cashier/login", "/cashier/login-process").permitAll()
                        .anyRequest().hasRole("CASHIER")
                )
                .formLogin(form -> form
                        .loginPage("/cashier/login")
                        .loginProcessingUrl("/cashier/login-process")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/cashier/", true)
                        .failureUrl("/cashier/login?error")
                )
                .userDetailsService(cashierDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/cashier/logout")
                        .logoutSuccessUrl("/cashier/login?logout")
                        .logoutSuccessHandler(roleBasedLogoutSuccessHandler)
                );
        return http.build();
    }

    @Bean
    public SecurityFilterChain fallbackSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/img/**").permitAll()
                        .anyRequest().denyAll()
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
