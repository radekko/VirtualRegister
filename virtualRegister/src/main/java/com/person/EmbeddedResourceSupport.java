package com.person;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class EmbeddedResourceSupport extends ResourceSupport {

	private final Map<String, ResourceSupport> embedded = new HashMap<String, ResourceSupport>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, ResourceSupport> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, ResourceSupport resource) {
    	embedded.put(relationship, resource);
    }
}