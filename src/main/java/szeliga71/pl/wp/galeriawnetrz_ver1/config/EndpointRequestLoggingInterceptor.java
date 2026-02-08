package szeliga71.pl.wp.galeriawnetrz_ver1.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.EndpointRequestCounterService;

import java.time.Instant;
import java.util.logging.Logger;

@Component
public class EndpointRequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(EndpointRequestLoggingInterceptor.class.getName());

    private final EndpointRequestCounterService counterService;

    public EndpointRequestLoggingInterceptor(EndpointRequestCounterService counterService) {
        this.counterService = counterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", Instant.now());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Instant start = (Instant) request.getAttribute("startTime");
        long duration = Instant.now().toEpochMilli() - start.toEpochMilli();

        int status = response.getStatus();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");

        boolean isSwagger = userAgent != null && userAgent.toLowerCase().contains("swagger");
        boolean success = status >= 200 && status < 300;
        String type = isSwagger ? "Swagger" : "API";

        counterService.increment(uri, type, success);

        logger.info(String.format("%s %s - status: %d - duration: %dms - UA: %s", method, uri, status, duration, userAgent));
    }
}
