package com.core;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.logging.AccessibleEndpoints;

@Component
public class EndpointsListener implements ApplicationListener<ApplicationEvent> {

	private final AccessibleEndpoints accessibleEndpoints;
	
    public EndpointsListener(AccessibleEndpoints accessibleEndpoints) {
    	this.accessibleEndpoints = accessibleEndpoints;
    }

	@Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
            applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach(accessibleEndpoints::storeEndpoint);
        }
        if(event instanceof ApplicationStartedEvent) {
        	accessibleEndpoints.printAllEndpoints();
        }
    }
}