package com.core;

import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMethod;

public class InfoAboutMethod implements Comparable<InfoAboutMethod>{
	private Optional<RequestMethod> requestMethod;
	private String methodName;
	
	public InfoAboutMethod(Optional<RequestMethod> requestMethod, String methodName) {
		this.requestMethod = requestMethod;
		this.methodName = methodName;
	}
	
	public Optional<RequestMethod> getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(Optional<RequestMethod> requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public int compareTo(InfoAboutMethod p) {
		if(p.requestMethod.isPresent())
			return -p.requestMethod.get().compareTo(this.requestMethod.get());
		return 0;
	}
}
