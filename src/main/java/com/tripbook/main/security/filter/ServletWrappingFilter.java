package com.tripbook.main.security.filter;

import com.tripbook.main.global.util.discord.RequestStorage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class ServletWrappingFilter extends OncePerRequestFilter {

    private final RequestStorage requestStorage;

    public ServletWrappingFilter(RequestStorage requestStorage) {
        this.requestStorage = requestStorage;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        requestStorage.set(wrappedRequest);

        filterChain.doFilter(wrappedRequest, response);
    }
}
