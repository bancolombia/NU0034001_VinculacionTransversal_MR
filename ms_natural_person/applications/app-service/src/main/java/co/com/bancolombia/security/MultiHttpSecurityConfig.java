package co.com.bancolombia.security;

import co.com.bancolombia.commonsvnt.model.commons.secretsmodel.SecretModelApiKeyInt;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class MultiHttpSecurityConfig {

    @Configuration
    @Order(1)
    public static class ProfileSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${spring.http.auth-token-header-name}")
        private String headerName;

        @Autowired
        private SecretModelApiKeyInt secretModelApiKeyInt;

        @SneakyThrows(Exception.class)
        @Override
        protected void configure(HttpSecurity httpSecurity) {
            APIKeyAuthFilter filter = FilterSecurityApi.filter(secretModelApiKeyInt, headerName);
            httpSecurity.authorizeRequests()
                    .antMatchers("/natural-person/api/v1/health").permitAll()
                    .and()
                    .antMatcher("/natural-person" + "/**/api/**").csrf().disable().exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().addFilter(filter).authorizeRequests().anyRequest().authenticated()
                    .and().authorizeRequests();
        }
    }

    @Configuration
    @Order(2)
    public static class DocumentationSecurityConfig extends WebSecurityConfigurerAdapter {
        @Value("${spring.http.auth-token-header-name}")
        private String headerName;

        @Autowired
        private SecretModelApiKeyInt secretModelApiKeyInt;

        @SneakyThrows(Exception.class)
        @Override
        protected void configure(HttpSecurity httpSecurity) {
            APIKeyAuthFilter filter = FilterSecurityApi.filter(secretModelApiKeyInt, headerName);
            httpSecurity.authorizeRequests()
                    .antMatchers("/natural-person/api/v1/health").permitAll()
                    .and()
                    .antMatcher("/natural-person" + "/**/api/**").csrf().disable().exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().addFilter(filter).authorizeRequests().anyRequest().authenticated()
                    .and().authorizeRequests();
        }
    }
}