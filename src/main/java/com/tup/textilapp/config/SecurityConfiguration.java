package com.tup.textilapp.config;

import com.tup.textilapp.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN))
                .and()
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.POST,"/authenticate").permitAll()

                        .requestMatchers(HttpMethod.GET,"/category").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.POST,"/category").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/category/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/category/subCategory/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/category/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/category/subCategory/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/product/listed").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.GET,"/product/listed/**").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.GET,"/product/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/product/all").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/product/all/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/product").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/product/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/product/unlist/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/product/list/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/order").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/order/pending").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/order/myOrders").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.POST,"/order").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.PUT,"/order/state/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/order/cancel/**").hasAnyAuthority("ADMIN", "CLIENT")

                        .requestMatchers(HttpMethod.GET,"/orderState").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/unit").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.GET,"/color").hasAnyAuthority("ADMIN", "CLIENT")

                        .requestMatchers(HttpMethod.GET,"/brand").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.POST,"/brand").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/brand/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/brand/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/payment/**").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.POST,"/payment/**").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.GET,"/payment/totalEarningsPerMonth").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/payment").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/payment-method").hasAnyAuthority("ADMIN", "CLIENT")

                        .requestMatchers("/stockMovement").hasAuthority("ADMIN")
                        .requestMatchers("/stockMovement/report").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/user").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.GET,"/user/ranking").hasAnyAuthority("ADMIN", "CLIENT")
                        .requestMatchers("/user/client").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

