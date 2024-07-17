package code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * handle business exception.
	 *
	 * @param businessException business exception
	 *
	 * @return ResponseResult
	 */
	@ResponseBody
	@ExceptionHandler(BusinessException.class)
	public String processBusinessException(BusinessException businessException) {
		log.error(businessException.getLocalizedMessage());
		return businessException.getMessage();
	}

	/**
	 * handle other exception.
	 *
	 * @param exception exception
	 *
	 * @return ResponseResult
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public String processException(Exception exception) {
		log.error(exception.getLocalizedMessage());
		return exception.getMessage();
	}
}
