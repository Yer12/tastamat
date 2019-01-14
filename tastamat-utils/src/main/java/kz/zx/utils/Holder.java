package kz.zx.utils;

/**
 * Created by deedarb on 11/1/17.
 */
public class Holder<T> {
	private T value;

	public Holder(){}

	public Holder(T value) {
		set(value);
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}
}
