package userservice.service.logging_aspect;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        logger.info("Start method: {} with arguments = {}",
                joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            logger.info("End method: {} with result = {}. Execution time = {} ms",
                    joinPoint.getSignature().toShortString(), result, endTime - startTime);

            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}. ",
                    Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().toShortString());

            throw e;
        }
    }
}
