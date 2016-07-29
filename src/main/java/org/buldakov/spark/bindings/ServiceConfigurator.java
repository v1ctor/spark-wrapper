package org.buldakov.spark.bindings;

import org.buldakov.spark.bindings.actions.ActionDescriptor;
import org.buldakov.spark.bindings.actions.ActionInvocationResolver;
import spark.ResponseTransformerRouteImpl;
import spark.Service;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceConfigurator {

    private final Service service;
    private final ActionInvocationResolver resolver;

    public ServiceConfigurator(Service service, ActionInvocationResolver resolver) {
        this.service = service;
        this.resolver = resolver;
    }

    public void init(List<Object> controllers) {
        List<ActionDescriptor> actionDescriptors = controllers.stream()
                .flatMap(controller -> resolver.resolve(controller).stream())
                .collect(Collectors.toList());
        for (ActionDescriptor descriptor : actionDescriptors) {
            //TODO logging
            System.out.println("Registered: " + descriptor.getHttpMethod().name() + " " + descriptor.getPath());
            service.addRoute(descriptor.getHttpMethod().name(),
                    ResponseTransformerRouteImpl.create(descriptor.getPath(), descriptor.getInvocation().getRoute(),
                            Object::toString));
        }
    }
}
