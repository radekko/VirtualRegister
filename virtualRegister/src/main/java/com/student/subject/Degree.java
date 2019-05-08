package com.student.subject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Degree {

	TWO(2.0f), THREE(3.0f), THREE_AND_HALF(3.5f), FOUR(4.0f), FOUR_AND_HALF(4.5f), FIVE(5.0f);

	private float value;

	Degree(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	private static final Map<Float, Degree> map;
	static {
		map = new HashMap<Float, Degree>();
		for (Degree d : Degree.values()) {
			map.put(d.value, d);
		}
	}

	public static Optional<Degree> getByValue(float degreeValue) {
		return Optional.ofNullable(map.get(degreeValue));
	}
}
