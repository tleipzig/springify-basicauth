package com.it_surmann.springify.basicauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;


/**
 * The configuration which initializes the {@link com.it_surmann.springify.basicauth.BasicAuthFilter}
 * with a FilterRegistrationBean.
 */
@Configuration
@ConditionalOnProperty(name = "springify.basicauth.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class BasicAuthFilterConfig {

    private BasicAuthProperties basicAuthProperties;

    @Autowired
    public BasicAuthFilterConfig(final BasicAuthProperties basicAuthProperties) {
        this.basicAuthProperties = basicAuthProperties;
    }

    @Bean
    public FilterRegistrationBean<BasicAuthFilter> basicAuthFilter() {
        log.info("Registering BasicAuthFilter with user {}", basicAuthProperties.getUsername());

        final FilterRegistrationBean<BasicAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        registrationBean.setFilter(new BasicAuthFilter(basicAuthProperties.getUsername(),
                basicAuthProperties.getPassword(), basicAuthProperties.getRealm()));
        registrationBean.setOrder(basicAuthProperties.getOrder());

        return registrationBean;
    }

}
