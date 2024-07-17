package code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RateLimitTestController {
	/**
	 * This method handles the web request and returns a response entity with a null body and an HTTP status of OK.
	 *
	 * @return a ResponseEntity with body and an HTTP status
	 */
	@RateLimit
	@ResponseBody
	@RequestMapping("/limit")
	public String limit() {
		log.info("limit");
		return "success";
	}

	@RateLimit(limit = 5)
	@GetMapping("/limit1")
	public String limit1() {
		log.info("limit1");
		return "success";
	}

	@GetMapping("/nolimit")
	public String noRateLimiter() {
		log.info("no limit");
		return "success";
	}
}
