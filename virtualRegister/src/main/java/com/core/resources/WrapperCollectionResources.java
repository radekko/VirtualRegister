package com.core.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

public interface WrapperCollectionResources<T extends ResourceSupport,E> {

	default Resources<T> prepareListResources(List<E> entities) {
		List<T> resources = entities.stream().map(this::entitytoResource).collect(Collectors.toList());
		return new Resources<>(resources,linkToGlobalCollection());
	}
	
	T entitytoResource(E entity);
	Link linkToGlobalCollection();
}
