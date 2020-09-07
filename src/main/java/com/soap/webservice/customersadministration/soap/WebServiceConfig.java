package com.soap.webservice.customersadministration.soap;

import java.util.Collections;
import java.util.List;

import javax.websocket.Endpoint;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
@EnableWs
public class WebServiceConfig extends WsConfigurerAdapter{

	
	@Bean
	public ServletRegistrationBean messageDispatcherServelet(ApplicationContext context) {
		MessageDispatcherServlet dispatcherServlet = new MessageDispatcherServlet();
		dispatcherServlet.setApplicationContext(context);
		dispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(dispatcherServlet, "/ws/*");
		
	}
	
	@Bean
	public XsdSchema cutomerSchema() {
		return new SimpleXsdSchema(new ClassPathResource("customer-information.xsd"));
	}
	
	@Bean(name = "customers")
	public DefaultWsdl11Definition  defaultWsdl11Definition(XsdSchema cutomerSchema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("CustomerPort");
		definition.setTargetNamespace("http://soap.webservice.com");
		definition.setLocationUri("/ws");
		definition.setSchema(cutomerSchema);
		return definition;
	}

	
	@Bean
	public XwsSecurityInterceptor securityInterceptor() {
		XwsSecurityInterceptor interceptor = new XwsSecurityInterceptor();
		interceptor.setCallbackHandler(callbackHandler());
		interceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
		return interceptor;
	}
	
	@Bean
	public SimplePasswordValidationCallbackHandler callbackHandler() {
		SimplePasswordValidationCallbackHandler handler = new SimplePasswordValidationCallbackHandler();
		handler.setUsersMap(Collections.singletonMap("fernando","123"));
		return handler;
	}
	
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(securityInterceptor());
	}
}
