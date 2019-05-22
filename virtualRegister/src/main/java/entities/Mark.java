package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Mark {

	TWO(2.0f), THREE(3.0f), THREE_AND_HALF(3.5f), FOUR(4.0f), FOUR_AND_HALF(4.5f), FIVE(5.0f);

	private float value;

	Mark(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	private static final Map<Float, Mark> map;
	static {
		map = new HashMap<Float, Mark>();
		for (Mark d : Mark.values()) {
			map.put(d.value, d);
		}
	}

	public static Optional<Mark> getByValue(float markValue) {
		return Optional.ofNullable(map.get(markValue));
	}
}