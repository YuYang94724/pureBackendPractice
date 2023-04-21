package com.yuyang.purebackendpractice.comfig.securtiy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableMongoAuditing
@Configuration
public class SpringSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final UserDetailsService springUserDetailService;

    //使用建構子注入方式注入token和userDetailService
    public SpringSecurityConfig(JwtTokenFilter jwtTokenFilter, UserDetailsService springUserDetailService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.springUserDetailService = springUserDetailService;
    }

    //用於提供自動獲取使用者的資料方法，使用在memberPOJO自動注入
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
    //用於提供密碼的加解密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //設置使用者方法和加解密方法的DAO驗證，會被注入到ProviderManager
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(springUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()//關閉 HTTP Basic 認證
                .formLogin().disable()//關閉表單登入
                .csrf().disable()//關閉跨站請求偽造 (CSRF)
                .logout().disable()//關閉登出功能
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//設定 session 管理策略為無狀態
                .and()
                .authorizeRequests()//指定授權給這些url
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1.0/members:login").permitAll()
                .anyRequest().authenticated();//任何請求皆需要驗證

        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
    //獲取AuthenticationManager並放入spring控管
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
