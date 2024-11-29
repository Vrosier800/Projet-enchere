package fr.eni.projet.ProjetEnchere.configuration.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class EniSecurityConfig {

    private static final String USER = "SELECT pseudo, mot_de_passe, 1 FROM utilisateurs WHERE pseudo = ? ";
    private static final String ADMIN = "SELECT u.pseudo, r.role FROM utilisateurs u INNER JOIN roles r "
            + " ON r.is_admin = u.administrateur WHERE u.pseudo = ? ";

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUser = new JdbcUserDetailsManager(dataSource);
        jdbcUser.setUsersByUsernameQuery(USER);
        jdbcUser.setAuthoritiesByUsernameQuery(ADMIN);

        return jdbcUser;
    }

    @Bean
    SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> {
            auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/inscription").permitAll()
                .requestMatchers("/forgotPassword").permitAll()
                .requestMatchers("/resetPassword/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/changePassword").permitAll()
                .requestMatchers("/css/*").permitAll()
                .requestMatchers("/js/*").permitAll()
                .requestMatchers("/imagesUtilisateurs/*").permitAll()
                .requestMatchers("/images/*").permitAll()
                .requestMatchers("/maxSessionExpiration").permitAll()
                .requestMatchers("/invalidSessionExpiration").permitAll()
                .requestMatchers(HttpMethod.POST, "/filtre").permitAll()
                .anyRequest().authenticated();
        });

        http.formLogin(form -> {
            form
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/session").permitAll();
        });
        
        http.sessionManagement(session -> {
        	session
            	.maximumSessions(1).expiredUrl("/maxSessionExpiration");
        	session.invalidSessionUrl("/invalidSessionExpiration");
        });

        http.logout(logout -> {
            logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();
        });

        return http.build();
    }

}
