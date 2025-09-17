package challenge.tools.util.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class NotFoundException extends RuntimeException {

	private String code;

	public NotFoundException(String code, String message) {
		super(message);
		this.code = code;
	}

	public static Supplier<NotFoundException> from(String messageCode, String message) {
		return () -> new NotFoundException(messageCode, message);
	}

}
