package ma.enset.mvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Utilisateurs en mémoire (mots de passe encodés avec BCrypt). */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(encoder.encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(encoder.encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(encoder.encode("1234")).roles("USER", "ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/user/index", true).permitAll())
                .rememberMe(rm -> rm.key("ap3-remember-me-key").tokenValiditySeconds(7 * 24 * 3600))
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/webjars/**", "/css/**", "/h2-console/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.accessDeniedPage("/notAuthorized"))
                // console H2 : frames autorisées et CSRF désactivé uniquement pour elle
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .build();
    }
}
