package com.logging;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

@Component
public class AccessibleEndpoints {
	private MultiValuedMap<String, InfoAboutMethod> endpoints = new HashSetValuedHashMap<String, InfoAboutMethod>();

	public void storeEndpoint(RequestMappingInfo r, HandlerMethod h) {
		String pathToEndpoint = extractPathToEndpoint(r.getPatternsCondition());
		Optional<RequestMethod> httpMethodName = extractEndpointMethod(r.getMethodsCondition());
		String methodName = h.getMethod().getName();
		
		InfoAboutMethod infoAboutMethod = new InfoAboutMethod(httpMethodName,methodName);
		endpoints.put(pathToEndpoint, infoAboutMethod);
	}
	
	public void printAllEndpoints() {
		Set<String> keySet = new TreeSet<>(endpoints.keySet());
		keySet.stream().forEach(this::printOneEndpoint);
		System.out.println();
	}

	private Optional<RequestMethod> extractEndpointMethod(RequestMethodsRequestCondition rm) {
		return rm.getMethods()
				.stream()
				.reduce(tooManyElementsException());
	}

	private String extractPathToEndpoint(PatternsRequestCondition pr) {
		return pr.getPatterns()
				.stream()
				.reduce(tooManyElementsException())
				.get();
	}
	
    private void printOneEndpoint(String pathToEndpoint) {
    	System.out.println(pathToEndpoint);
    	System.out.println("---------------------------------------------------------------------");
    	endpoints.get(pathToEndpoint).stream().sorted().forEach(this::print);
    	System.out.println();
    }
    
    private void print(InfoAboutMethod i) {
    	String httpMethodName = i.getRequestMethod().map(Enum::toString).orElse("");
    	System.out.format("%-5s| %s \n", httpMethodName, i.getServerSideMethodName());
    }
	
	private <T> BinaryOperator<T> tooManyElementsException() {
		return (a, b) -> {
			throw new IllegalStateException("Multiple elements: " + a + ", " + b);
		};
	}
	
}
