package logging;

import java.util.Optional;
import java.util.function.BinaryOperator;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

@Component
public class AccessibleEndpoints {
	private final Endpoints endpoints;

	public AccessibleEndpoints(Endpoints endpoints) {
		this.endpoints = endpoints;
	}

	public void storeEndpoint(RequestMappingInfo reqInfo, HandlerMethod handler) {
		String pathToEndpoint = extractPathToEndpoint(reqInfo.getPatternsCondition());
		Optional<RequestMethod> httpMethodName = extractEndpointMethod(reqInfo.getMethodsCondition());
		String methodName = handler.getMethod().getName();
		
		InfoAboutMethod infoAboutMethod = new InfoAboutMethod(httpMethodName,methodName);
		endpoints.put(pathToEndpoint, infoAboutMethod);
	}
	
	public void printAllEndpoints() {
		endpoints.printAllEndpoints();
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
	
	private <T> BinaryOperator<T> tooManyElementsException() {
		return (a, b) -> {
			throw new IllegalStateException("Multiple elements: " + a + ", " + b);
		};
	}
}