package br.com.zup.dmagliano.proposta.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@Profile(value = {"dev", "prod"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/actuator/**")
                .permitAll()
                .and()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers(HttpMethod.GET, "/propostas/**").hasAuthority("SCOPE_login-scope")
                                .antMatchers(HttpMethod.POST, "/propostas/**").hasAuthority("SCOPE_login-scope")
                                .antMatchers(HttpMethod.GET, "/cartoes/**").hasAuthority("SCOPE_login-scope")
                                .antMatchers(HttpMethod.POST, "/cartoes/**").hasAuthority("SCOPE_login-scope")
                                .antMatchers(HttpMethod.GET, "/biometria/**").hasAuthority("SCOPE_login-scope")
                                .antMatchers(HttpMethod.POST, "/biometria/**").hasAuthority("SCOPE_login-scope")
                                .anyRequest().authenticated()
                ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

}
