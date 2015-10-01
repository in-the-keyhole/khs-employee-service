package khs.employee.boot;


import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import khs.service.discovery.EurekaRegistry;
import khs.service.metrics.ApiHealthCheckServletContextListener;
import khs.service.metrics.MetricsContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import com.codahale.metrics.servlets.AdminServlet;
import com.khs.sherpa.SherpaContextListener;
import com.khs.sherpa.servlet.SherpaServlet;

@ImportResource("classpath*:META-INF/spring/application-context*.xml")
public class Application implements ServletContextInitializer {

	public static void main(final String[] args) {
		System.out.println(Application.class.getName() + "#main");
	    SpringApplication.run(Application.class, args);
	}

	@Autowired
	private Environment environment;
	
	@Autowired
	private EurekaRegistry registry;

    @Bean
    public EmbeddedServletContainerFactory getEmbeddedServletContainerFactory() {
		System.out.println(Application.class.getName() + "#getEmbeddedServletContainerFactory");
		// e.g. command line arg: java -jar <WAR_NAME> --port=8099
		final int port = environment.getProperty("port", int.class, 8082);
    	JettyEmbeddedServletContainerFactory factory =  new JettyEmbeddedServletContainerFactory(port);
	//	factory.setSessionTimeout(10, TimeUnit.SECONDS);
    	return factory;
    }

    @Override
	public void onStartup(final ServletContext servletContext)
			throws ServletException {
		System.out.println(Application.class.getName() + "#onStartup");
		final FilterRegistration urlRewritefilterRegistration = servletContext.addFilter(
				UrlRewriteFilter.class.getSimpleName(),
				UrlRewriteFilter.class);
		urlRewritefilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "*");
		final FilterRegistration crossOriginFilterRegistration = servletContext.addFilter(
				"SimpleCORSFilter",
				new Filter() { //https://spring.io/guides/gs/rest-service-cors/
				public void doFilter(final ServletRequest req,
									 final ServletResponse res,
									 final FilterChain chain) throws IOException, ServletException {
					final HttpServletResponse response = (HttpServletResponse) res;
					
					HttpServletRequest request = (HttpServletRequest) req;
					String clientOrigin = request.getHeader("origin");
					if (clientOrigin != null) {
						response.setHeader("Access-Control-Allow-Origin", clientOrigin);
					}
					else {
						response.setHeader("Access-Control-Allow-Origin", "*");
					}
					response.setHeader("Access-Control-Allow-Credentials", "true");
					response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
					response.setHeader("Access-Control-Allow-Credentials", "true");
					response.setHeader("Access-Control-Max-Age", "3600");
					response.setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type, userid, token");
					chain.doFilter(req, res);
				}
				public void init(final FilterConfig filterConfig) {}
				public void destroy() {}
		});
		crossOriginFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/api/*");
		servletContext.getServletRegistration("default").addMapping("/assets/*");
		servletContext.setInitParameter("sherpaConfigLocation", "classpath:sherpa.properties");
		servletContext.addListener(SherpaContextListener.class.getName());
		ServletRegistration servletRegistration = servletContext.addServlet("sherpa", SherpaServlet.class);
		servletRegistration.addMapping("/api");
		servletRegistration.addMapping("/api/*");
		
		servletContext.addListener(MetricsContextListener.class.getName());
		servletRegistration = servletContext.addServlet("metrics", AdminServlet.class);
		servletRegistration.addMapping("/metrics/*");
		
		servletContext.addListener(ApiHealthCheckServletContextListener.class.getName());
	
		
    
		registry.registerAndStart();
    }
    

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}