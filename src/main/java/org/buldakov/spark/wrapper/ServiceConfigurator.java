package org.buldakov.spark.wrapper;

import org.buldakov.spark.wrapper.actions.ActionDescriptor;
import org.buldakov.spark.wrapper.actions.ActionInvocationResolver;
import org.buldakov.spark.wrapper.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ResponseTransformer;
import spark.ResponseTransformerRouteImpl;
import spark.Route;
import spark.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfigurator.class);

    private final Service service;
    private final ActionInvocationResolver resolver;
    private final List<Object> controllers;
    private final ResponseTransformer transformer;

    public ServiceConfigurator(Service service, ActionInvocationResolver resolver,
                               List<Object> controllers,
                               ResponseTransformer transformer)
    {
        this.service = service;
        this.resolver = resolver;
        this.controllers = controllers;
        this.transformer = transformer;
    }

    public void init() {

        List<ActionDescriptor> actionDescriptors = controllers.stream()
                .flatMap(controller -> resolver.resolve(controller).stream())
                .collect(Collectors.toList());

        for (ActionDescriptor descriptor : actionDescriptors) {
            LOGGER.info("Registered: " + descriptor.getHttpMethod().name() + " " + descriptor.getPath());
            service.addRoute(descriptor.getHttpMethod().name(),
                    ResponseTransformerRouteImpl.create(descriptor.getPath(), getRoute(descriptor), transformer));
        }
    }

    private Route getRoute(ActionDescriptor descriptor) {
        return (request, response) -> {
            Object answer = descriptor.getInvocation().getRoute().handle(request, response);
            if (answer instanceof Result<?>) {
                Result<?> result = (Result<?>) answer;
                response.status(result.getCode());
                return result.getValue();
            }
            return answer;
        };
    }
}
