package kz.zx.api.enums;

/**
 * Created by deedarb on 8/1/17.
 */
public enum Errors {

	UNEXPECTED(100),
	JSON_MAP(101),
	NOT_FOUND(102),
	BUSINESS(580),
	ACCESS(103),
	DATA(400),
	UNAUTHORIZED(401);

	private int code;

	Errors(int code) {
		this.code = code;
	}

	public int code() {
		return this.code;
	}
}
