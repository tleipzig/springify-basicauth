package com.it_surmann.springify.basicauth;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;


/**
 * A servlet filter implementation that checks incoming traffic for proper basic authentication credentials.
 */
@Slf4j
public class BasicAuthFilter implements Filter {

    private String basicUsername;
    private String basicPassword;
    private String basicRealm;

    @Autowired
    public BasicAuthFilter(final String basicUsername, final String basicPassword, final String basicRealm) {
        if (StringUtils.isEmpty(basicUsername) || StringUtils.isEmpty(basicPassword)) {
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

        final String authorizationString = new String(Base64.decodeBase64(tokenizer.nextToken()), "UTF-8");
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
