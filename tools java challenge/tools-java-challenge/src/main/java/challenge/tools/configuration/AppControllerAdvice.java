package challenge.tools.configuration;

import challenge.tools.util.exception.NotFoundException;
import challenge.tools.util.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AppControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerAdvice.class);

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		LOGGER.error("Exceção não tratada pela aplicação", e);
		return ResponseEntity.status(e.getStatusCode()).contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(BAD_REQUEST,
						"Erro na validação dos dados da requisição"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeralException(Exception e) {
		LOGGER.error("Exceção não tratada pela aplicação", e);
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorResponse(INTERNAL_SERVER_ERROR,
						"Ocorreu um erro inesperado!"));
	}
}
