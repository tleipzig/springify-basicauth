package com.it_surmann.springify.basicauth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;


/**
 * A servlet filter implementation that checks incoming traffic for proper basic authentication credentials.
 */
@Slf4j
public class BasicAuthFilter implements Filter {

    private final String basicUsername;
    private final String basicPassword;
    private final String basicRealm;

    public BasicAuthFilter(final String basicUsername, final String basicPassword, final String basicRealm) {
        if (ObjectUtils.isEmpty(basicUsername) || ObjectUtils.isEmpty(basicPassword)) {
            throw new RuntimeException("Missing username or password for basic authentication. Please " +
                    "specify as \"springify.basicauth.username\" and \"springify.basicauth.password\" or " +
                    "disable with \"springify.basicauth.enabled=false\".");
        }
        this.basicUsername = basicUsername;
        this.basicPassword = basicPassword;
        this.basicRealm = basicRealm;
    }

    @Override
    public void init(final FilterConfig filterConfig) {
        // nothing to initialize
    }

    @Override
    @SneakyThrows
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) {
        final HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader == null) {
            respond401(httpServletResponse, "Unauthorized.");
            return;
        }

        final StringTokenizer tokenizer = new StringTokenizer(authorizationHeader);
        if (!tokenizer.hasMoreTokens()) {
            respond401(httpServletResponse, "Unauthorized.");
            return;
        }

        final String authorizationType = tokenizer.nextToken();
        if (!authorizationType.equalsIgnoreCase("Basic")) {
            respond401(httpServletResponse, "Wrong authentication type: " + authorizationType + ".");
            return;
        }

        final byte[] authorizationBytes = Base64.getDecoder().decode(tokenizer.nextToken());
        final String authorizationString = new String(authorizationBytes, StandardCharsets.UTF_8);
        final int c = authorizationString.indexOf(":");
        if (c == -1) {
            respond401(httpServletResponse, "Invalid authentication string.");
        }

        final String givenUsername = authorizationString.substring(0, c).trim();
        final String givenPassword = authorizationString.substring(c + 1).trim();

        if (!basicUsername.equals(givenUsername) || !basicPassword.equals(givenPassword)) {
            respond401(httpServletResponse, "Bad credentials.");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // nothing to destroy
    }

    @SneakyThrows
    private void respond401(final HttpServletResponse httpServletResponse, final String errorMessage) {
        log.debug("Couldn't authenticate request: {}", errorMessage);

        httpServletResponse.setHeader("WWW-Authenticate", "Basic realm=\"" + basicRealm + "\"");
        httpServletResponse.sendError(401, errorMessage);
    }

}
