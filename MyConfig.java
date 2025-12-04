package com.springsecurity.demo.configuration;

import com.springsecurity.demo.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class MyConfig {
    @Autowired
    BankUserDetailsService userDetailsService;
    @Autowired
    JwtFilter jwtFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((request)->request.requestMatchers("/register","/myNotices","/myContact","/error","/userLogin").permitAll().requestMatchers("/myAccount","/myLoans","/myCards","/myBalance").authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(withDefaults())
            .httpBasic(withDefaults())
            .csrf((csrfConfig)->csrfConfig.disable());
            return http.build();
    }
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
////        UserDetails user = User.withUsername("user").password("{bcrypt}$2a$12$.hliCMdkTSCP0LEbvmSRTuRsQyNSDUH8bYpdWlXte0Qj48jpFoehW").authorities("read").build();
////        UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$9/N.oDRvfvzvhU5ycE/TBeNdjpOs5ILj5w3yYuYnFDJ3leR5fpJ9C").authorities("admin").build();
////        return new InMemoryUserDetailsManager(user,admin);
//        return new JdbcUserDetailsManager(dataSource);
//    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider provider =new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    return provider;
}
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }
}
