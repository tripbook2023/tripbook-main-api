package com.tripbook.main.global.util.discord;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.joining;

@Component
public class DiscordMessageGenerator {

    private static final String EXTRACTION_ERROR_MESSAGE = "메세지를 추출하는데 오류가 생겼습니다.\nmessagee : %s";
    private static final String EXCEPTION_MESSAGE_FORMAT = "_%s_ %s.%s:%d - %s";
    private static final String DISCORD_MESSAGE_FORMAT = "*[%s]* %s\n\n*[ERROR LOG]*\n%s\n\n*[REQUEST_INFORMATION]*\n%s %s\n%s\n\n*[REQUEST_BODY]*\n%s\n\n*[REQUEST_PARMETER]*\n%s";
    private static final String EMPTY_BODY_MESSAGE = "{BODY IS EMPTY}";

    private final Environment environment;

    public DiscordMessageGenerator(Environment environment) {
        this.environment = environment;
    }

    public String generate(ContentCachingRequestWrapper request,
                           Exception exception) {
        try {
            String profile = getProfile();
            String currentTime = getCurrentTime();
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            String headers = extractHeaders(request);
            String body = getBody(request);
            String param = getParameter(request);
            String exceptionMessage = extractExceptionMessage(exception);

            return toMessage(profile, currentTime, exceptionMessage, method, requestURI, headers, body, param);
        } catch (Exception e) {
            return String.format(EXTRACTION_ERROR_MESSAGE, e.getMessage());
        }
    }

    private String getProfile() {
        return String.join(",", environment.getActiveProfiles()).toUpperCase();
    }

    private String getCurrentTime() {
        return LocalDateTime.now().toString();
    }

    private String getParameter(ContentCachingRequestWrapper request) {
        Map<String, String[]> values = request.getParameterMap();
        return values.entrySet().stream()
                .map(e -> e.getKey() + ":" + Arrays.toString(e.getValue()))
                .collect(joining("\n"));
    }

    private String extractHeaders(ContentCachingRequestWrapper request) {
        Enumeration<String> headerNames = request.getHeaderNames();

        Map<String, String> values = new HashMap<>();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            values.put(headerName, request.getHeader(headerName));
        }

        return values.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(joining("\n"));
    }

    private String getBody(ContentCachingRequestWrapper request) {
        String body = new String(request.getContentAsByteArray());
        if (body.isEmpty()) {
            body = EMPTY_BODY_MESSAGE;
        }
        return body;
    }

    private String extractExceptionMessage(Exception e) {
        StackTraceElement stackTrace = e.getStackTrace()[0];
        String className = stackTrace.getClassName();
        int lineNumber = stackTrace.getLineNumber();
        String methodName = stackTrace.getMethodName();

        String message = e.getMessage();

        if (Objects.isNull(message)) {
            return Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(joining("\n"));
        }

        return String
                .format(EXCEPTION_MESSAGE_FORMAT, "ERROR", className, methodName, lineNumber,
                        message);
    }


    private String toMessage(String profile, String currentTime, String errorMessage,
                             String method, String requestURI, String headers, String body, String param) {
        return String.format(
                DISCORD_MESSAGE_FORMAT, profile, currentTime,
                errorMessage, method, requestURI, headers, body, param
        );
    }
}