package logging;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.stereotype.Component;

@Component
public class Endpoints {
	private final MultiValuedMap<String, InfoAboutMethod> endpoints;

	public Endpoints() {
		endpoints = new HashSetValuedHashMap<>();
	}
	
	public void printAllEndpoints() {
		endpoints.keySet().stream().sorted().forEach(this::printOneEndpoint);
	}

	public boolean put(String pathToEndpoint, InfoAboutMethod infoAboutMethod) {
		return endpoints.put(pathToEndpoint, infoAboutMethod);
	}
	
	private void printOneEndpoint(String pathToEndpoint) {
    	System.out.println(pathToEndpoint);
    	System.out.println("---------------------------------------------------------------------");
    	endpoints.get(pathToEndpoint).stream().sorted().map(InfoAboutMethod::toString).forEach(System.out::println);
    	System.out.println();
    }
}