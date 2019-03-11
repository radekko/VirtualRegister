package com.core;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class AccessibleEndpoints {
	private MultiValuedMap<String, Optional<RequestMethod>> endpoints = new HashSetValuedHashMap<String, Optional<RequestMethod>>();

	public void storeEndpoint(RequestMappingInfo r, HandlerMethod h) {
		String singlePath = extractPathToEndpoint(r);
		Optional<RequestMethod> httpMethodName = extractEndpointMethod(r);
		
		endpoints.put(singlePath, httpMethodName);
	}
	
	public void printAllEndpoints() {
		Set<String> keySet = new TreeSet<>(endpoints.keySet());
		keySet.stream().forEach(this::printOneEndpoint);
	}

	private Optional<RequestMethod> extractEndpointMethod(RequestMappingInfo r) {
		return r.getMethodsCondition().getMethods()
				.stream()
				.reduce(tooManyElementsException());
	}

	private String extractPathToEndpoint(RequestMappingInfo r) {
		return r.getPatternsCondition().getPatterns()
				.stream()
				.reduce(tooManyElementsException())
				.get();
	}
	
    private void printOneEndpoint(String pathToEndpoint) {
        List<RequestMethod> listWithMethods = endpoints.get(pathToEndpoint)
    		  					  .stream()
    		  					  .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
    		  					  .collect(Collectors.toList());
    	
    	Collections.sort(listWithMethods);
    	System.out.format("%-45s| %s \n", pathToEndpoint, listWithMethods);
    }
	
	private <T> BinaryOperator<T> tooManyElementsException() {
		return (a, b) -> {
			throw new IllegalStateException("Multiple elements: " + a + ", " + b);
		};
	}
	
}
