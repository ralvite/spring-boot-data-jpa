package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

// !Importante para usas @Secured("ROLE_ADMIN") en los controladores
// Una alternativa igual a @Secured es prePostEnabled (se habilita también) pero solo con secured sería suficiente
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // lo traemos desde MvcConfig

    // @Autowired
    // private DataSource dataSource;

    @Autowired
    private LoginSuccessHandler successHandler;

    // AUTENTICACION Y ENCRIPTACIÓN DE CLAVES
    // se ha movido a MvcConfig (puede estar en cualquier clase anotada como Config)
    // @Bean
    // public static BCryptPasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

    // AUTENTICACIÓN CON USUARIOS EN MEMORIA
    // -----------------------------------------------------------
    // Dejamos de usar la creación de usuarios desde memoria a
    // extraerlos de la base de datos con JDBC

    // @Bean
    // public UserDetailsService userDetailsService() throws Exception {

    // InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    // manager.createUser(User.withUsername("user")
    // .password(passwordEncoder.encode("user"))
    // .roles("USER").build());

    // manager.createUser(User.withUsername("admin")
    // .password(passwordEncoder.encode("admin"))
    // .roles("ADMIN", "USER").build());

    // return manager;
    // }

    // Autenticación con JDBC
    // NO FUNCIONA! seguir investigando.......
    // -----------------------------------------------------------

    // @Bean
    // AuthenticationManager authManager(HttpSecurity http) throws Exception {
    // return http.getSharedObject(AuthenticationManagerBuilder.class)
    // .jdbcAuthentication()
    // .dataSource(dataSource)
    // .passwordEncoder(passwordEncoder)
    // .usersByUsernameQuery("select username, password, enabled from users where
    // username=?")
    // .authoritiesByUsernameQuery("select u.username, a.authority from authorities
    // a inner join users u on (a.user_id=u.id) where u.username=?")
    // and().build();
    // }


    // AUTENTICACIÓN CON JPA
    // -----------------------------------------------------------
    @Autowired
    private JpaUserDetailsService userDetailService;

    @Autowired
    public void userDetailsService(AuthenticationManagerBuilder build) throws
    Exception {
    build.userDetailsService(userDetailService)
    .passwordEncoder(passwordEncoder);
    }



    // @Bean
    // public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
    //     AuthenticationManagerBuilder authenticationManagerBuilder = http
    //             .getSharedObject(AuthenticationManagerBuilder.class);
    //     authenticationManagerBuilder.userDetailsService(userDetailService);
    //             // .passwordEncoder(passwordEncoder);
    //     return authenticationManagerBuilder.build();
    // }



    // AUTORIZACION
    // Una alternativa a autorizar en los requestMatchers siguientes
    // es usando anotaciones @Secured en el controlador
    // las siguientes líneas comentadas se autorizan en el controlador
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                (authz) -> authz
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                        // .requestMatchers("/ver/**").hasAnyRole("USER")
                        // .requestMatchers("/uploads/**").hasAnyRole("USER")
                        // .requestMatchers("/form/**").hasAnyRole("ADMIN")
                        // .requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
                        // .requestMatchers("/factura/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login").successHandler(successHandler).permitAll()) // formulario
                                                                                                          // login con
                                                                                                          // vista
                                                                                                          // personalizada
                                                                                                          // y logout
                .logout(logout -> logout.permitAll())
                .exceptionHandling((exception) -> exception.accessDeniedPage("/error_403"));

        return http.build();
    }



}
