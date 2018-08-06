package com.snippet.rxjava;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringComponentProvider;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ComponentScan({"com.snippet.rxjava"})
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        super(RxJavaResourceImpl.class);
        register(RequestContextFilter.class);
        //packages("com.snippet.rxjava");
        register(JacksonFeature.class);
        register(RequestContextFilter.class);
        register(LoggingFilter.class);
        register(SpringComponentProvider.class);
    }

}
