package org.buldakov.spark.wrapper;

import org.buldakov.spark.wrapper.actions.ActionDescriptor;
import org.buldakov.spark.wrapper.actions.ActionInvocationResolver;
import org.buldakov.spark.wrapper.result.Result;
import spark.ResponseTransformer;
import spark.ResponseTransformerRouteImpl;
import spark.Route;
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

    public void init(List<Object> controllers, ResponseTransformer transformer) {

        List<ActionDescriptor> actionDescriptors = controllers.stream()
                .flatMap(controller -> resolver.resolve(controller).stream())
                .collect(Collectors.toList());

        for (ActionDescriptor descriptor : actionDescriptors) {
            //TODO logging
            System.out.println("Registered: " + descriptor.getHttpMethod().name() + " " + descriptor.getPath());

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
