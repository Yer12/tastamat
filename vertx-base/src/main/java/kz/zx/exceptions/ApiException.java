package kz.zx.exceptions;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import kz.zx.api.enums.Errors;
import kz.zx.utils.IdGenerator;

import java.util.function.Supplier;

/**
 * Created by deedarb on 8/1/17.
 */
public class ApiException extends RuntimeException {

	public String id;
	public Errors error;

	public ApiException() {
		this.id = errorId();
	}

	public ApiException(Errors error) {
		this.error = error;
		this.id = errorId();
	}

	public ApiException(Errors error, String message) {
		super(message);
		this.error = error;
		this.id = errorId();
	}

	public ApiException(Errors error, Throwable throwable) {
		super(throwable);
		this.error = error;
		this.id = errorId();
	}

	public ApiException(Errors error, String message, Throwable throwable) {
		super(message, throwable);
		this.error = error;
		this.id = errorId();
	}

	private static String errorId() {
		return IdGenerator.getBase62(8);
	}

	@Override
	public String getMessage() {
		JsonObject jsonObject = new JsonObject()
				.put("id", this.id)
				.put("code", this.error.code())
				.put("message", super.getMessage());

		return jsonObject.toString();
	}

	public String getErrorMessage() {
		return super.getMessage();
	}

	public static ApiException build(Errors error, Throwable ex) {
		if (ex instanceof ApiException) {
			return (ApiException) ex;
		}
		return new ApiException(error, ex);
	}

	public static ApiException build(Errors error, String message, Throwable ex) {
		if (ex instanceof ApiException) {
			return (ApiException) ex;
		}
		return new ApiException(error, message, ex);
	}

	public static ApiException build(Errors error, String message) {
		return new ApiException(error, message);
	}

	public static ApiException notFound(String message) {
		return new ApiException(Errors.NOT_FOUND, message);
	}

	public static ApiException unexpected(String message) {
		return new ApiException(Errors.UNEXPECTED, message);
	}

	public static ApiException unexpected(String message, Throwable ex) {
		return new ApiException(Errors.UNEXPECTED, message, ex);
	}

	public static ApiException forbidden(String message) {
		return new ApiException(Errors.ACCESS, message);
	}

	public static ApiException unauthorized(String message) {
		return new ApiException(Errors.UNAUTHORIZED, message);
	}

	public static ApiException data(String message) {
		return new ApiException(Errors.DATA, message);
	}

	public static ApiException business(String message) {
		return new ApiException(Errors.BUSINESS, message);
	}

	public static Supplier<ApiException> notFoundFunc(String text) {
		return () -> ApiException.notFound(text);
	}
}
