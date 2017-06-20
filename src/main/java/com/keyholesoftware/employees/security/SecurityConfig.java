package com.keyholesoftware.employees.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Configuration
	static class FilterConfig {
		
		public FilterConfig() {
			super();
		}
		
		/**
		 * This keeps X509ValidCertificateFilter from being registered twice
		 * 
		 * @see http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-disable-registration-of-a-servlet-or-filter
		 * 
		 * @param filter
		 * @return
		 */
		@Bean
		public FilterRegistrationBean registration(X509ValidCertificateFilter filter) {
		    FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		    registration.setEnabled(false);
		    return registration;
		}
		
		@Bean("certFilter")
		@ConditionalOnProperty(name = "server.ssl.enabled", havingValue = "true")
		X509ValidCertificateFilter x509Filter() {
			return new X509ValidCertificateFilter();
		}

		@Bean("certFilter")
		@ConditionalOnProperty(name = "server.ssl.enabled", havingValue = "false")
		X509ValidCertificateFilter noopFilter() {
			return new X509ValidCertificateFilter() {

				@Override
				public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
					chain.doFilter(request, response);
				}
			};
		}
	}

	@Autowired
	private JWTAuthenticationService authenticationService;

	@Autowired
	@Qualifier("certFilter")
	private X509ValidCertificateFilter certFilter;
	
	@Bean JWTVerificationStrategy jwtVerificationStrategy() {
		return new RSA256VerificationStrategy();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers(HttpMethod.GET, "/health");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/api/**")
					.authenticated()
			.and()
				.addFilterBefore(certFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JWTAuthenticationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class)
			.csrf()
				.disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}