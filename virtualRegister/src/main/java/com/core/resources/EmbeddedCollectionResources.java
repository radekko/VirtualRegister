package com.core.resources;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

public interface EmbeddedCollectionResources<T extends ResourceSupport,E> extends WrapperCollectionResources<T,E>{
	
	default Resources<T> prepareListResources(List<E> entities) {
		Resources<T> resources = WrapperCollectionResources.super.prepareListResources(entities);
		resources.add(linkToParent());
		return resources;
	}
	
	Link linkToParent();
}
