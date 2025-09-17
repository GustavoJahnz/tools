package challenge.tools.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

	private HttpStatus status;
	private String code;
	private String message;
	private List<ErrorResponseItem> errors;

	public ErrorResponse() {
		this.errors = new ArrayList<>(0);
	}

	public ErrorResponse(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public ErrorResponse(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

    public void setErrors(List<ErrorResponseItem> errors) {
		this.errors = errors;
	}

}
