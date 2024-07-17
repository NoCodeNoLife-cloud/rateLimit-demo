package code;

import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {
	private final ConcurrentHashMap<String, RateLimiter> EXISTED_RATE_LIMITERS = new ConcurrentHashMap<>();

	/**
	 * This method acts as a facade to wrap around the execution of methods.
	 *
	 * @param proceedingJoinPoint The proceeding join point
	 *
	 * @return The result of the method execution
	 */
	@Around("@annotation(RateLimit)")
	@SneakyThrows
	public Object rateLimit(ProceedingJoinPoint proceedingJoinPoint) {
		MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		Method method = signature.getMethod();
		RateLimit annotation = AnnotationUtils.findAnnotation(method, RateLimit.class);

		// get rate limiter
		RateLimiter rateLimiter = EXISTED_RATE_LIMITERS.computeIfAbsent(method.getName(), k -> RateLimiter.create(annotation.limit()));

		// process
		if (rateLimiter.tryAcquire()) {
			return proceedingJoinPoint.proceed();
		} else {
			throw new RuntimeException("too many requests, please try again later...");
		}
	}
}