package org.buldakov.spark.bindings.actions;

import spark.route.HttpMethod;

public class ActionDescriptor {

    private final HttpMethod httpMethod;
    private final String path;
    private final ActionInvocation invocation;

    public ActionDescriptor(HttpMethod httpMethod, String path, ActionInvocation invocation) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.invocation = invocation;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public ActionInvocation getInvocation() {
        return invocation;
    }
}
