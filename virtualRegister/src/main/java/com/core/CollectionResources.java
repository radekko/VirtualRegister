package com.core;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

public interface CollectionResources<T extends ResourceSupport,E> {
	public default Resources<T> toGlobalResource(List<E> entities, Link linkToCollection) {
		List<T> resources = entities.stream().map(this::toResource).collect(Collectors.toList());
		return new Resources<>(resources,linkToCollection);
	}
	
	public abstract T toResource(E entity);
}
