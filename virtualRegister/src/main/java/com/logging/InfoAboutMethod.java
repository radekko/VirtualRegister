package com.logging;

import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMethod;

public class InfoAboutMethod implements Comparable<InfoAboutMethod>{
	private Optional<RequestMethod> requestMethod;
	private String serverSideMethodName;
	
	public InfoAboutMethod(Optional<RequestMethod> requestMethod, String serverSideMethodName) {
		this.requestMethod = requestMethod;
		this.serverSideMethodName = serverSideMethodName;
	}
	
	public Optional<RequestMethod> getRequestMethod() {
		return requestMethod;
	}
	
	public void setRequestMethod(Optional<RequestMethod> requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public String getServerSideMethodName() {
		return serverSideMethodName;
	}
	
	public void setMethodName(String methodName) {
		this.serverSideMethodName = methodName;
	}

	@Override
	public int compareTo(InfoAboutMethod p) {
		if(isTheSame(p) || !this.requestMethod.isPresent())
			return 0;

		return p.requestMethod.map(this::compare).orElse(0);
	}

	private boolean isTheSame(InfoAboutMethod p) {
		return p.requestMethod.equals(this.requestMethod);
	}
	
	private int compare(RequestMethod rm) {
		return -rm.compareTo(this.requestMethod.get());
	}

	@Override
	public String toString() {
    	String httpMethodName = requestMethod.map(Enum::toString).orElse("");
    	return String.format("%-5s| %s", httpMethodName, serverSideMethodName);
	}
	
}
