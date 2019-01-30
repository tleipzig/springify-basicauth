package com.it_surmann.springify.basicauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/**
 * A component holding the configuration properties for springify-basicauth.
 */
@Component
@Getter
public class BasicAuthProperties {

    @Value("${springify.basicauth.username:}")
    private String username;

    @Value("${springify.basicauth.password:}")
    private String password;

    @Value("${springify.basicauth.realm:Restricted}")
    private String realm;

    @Value("${springify.basicauth.order:" + (Ordered.HIGHEST_PRECEDENCE + 5) + "}")
    private Integer order;

}
