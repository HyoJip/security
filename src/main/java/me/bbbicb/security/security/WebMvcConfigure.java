package me.bbbicb.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigure extends WebMvcConfigurerAdapter {

  public static final String LOGIN_PAGE_URL = "/login";

  @Bean
  public LoginSuccessHandler loginSuccessHandler(ObjectMapper om) {
    return new LoginSuccessHandler(om);
  }

  @Bean
  public LoginFailHandler loginFailHandler(ObjectMapper om) {
    return new LoginFailHandler(om);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new MyUserDetailService();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler(ObjectMapper om) {
    return new CommonAccessDeniedHandler(om);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Order(1)
  @Configuration
  @RequiredArgsConstructor
  public static class SecurityConfigure extends WebSecurityConfigurerAdapter {

    private final ObjectMapper om;
    private final AccessDeniedHandler accessDeniedHandler;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailHandler loginFailHandler;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .csrf().disable()
        .formLogin().disable()
        .exceptionHandling()
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_PAGE_URL))
        .accessDeniedHandler(accessDeniedHandler)
        .and()
        .authorizeHttpRequests()
        .antMatchers("/sso/login", LOGIN_PAGE_URL).permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .and()
        .and()
        .addFilterAt(jsonSecurityFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
      return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
      auth.authenticationProvider(myAuthenticationProvider());
    }

    @Bean
    public MyAuthenticationProvider myAuthenticationProvider() {
      return new MyAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    private JsonSecurityFilter jsonSecurityFilter() throws Exception {
      JsonSecurityFilter filter = new JsonSecurityFilter(om);
      filter.setAuthenticationManager(authenticationManager());
      filter.setAuthenticationSuccessHandler(loginSuccessHandler);
      filter.setAuthenticationFailureHandler(loginFailHandler);
      filter.setFilterProcessesUrl("/sso/login");
      return filter;
    }

  }

  @Order(0)
  @Configuration
  public static class ExternalSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .antMatcher("/ext/**")
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication()
        .withUser("user").password("password").roles("USER");
    }
  }
}
