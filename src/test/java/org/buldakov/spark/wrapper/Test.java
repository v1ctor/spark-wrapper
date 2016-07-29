package org.buldakov.spark.wrapper;

import org.buldakov.spark.wrapper.actions.ActionInvocationResolver;
import org.buldakov.spark.wrapper.annotations.Action;
import org.buldakov.spark.wrapper.annotations.ActionController;
import org.buldakov.spark.wrapper.annotations.Body;
import org.buldakov.spark.wrapper.annotations.GetParam;
import org.buldakov.spark.wrapper.annotations.HeaderParam;
import org.buldakov.spark.wrapper.annotations.PathParam;
import org.buldakov.spark.wrapper.converters.TypeConverter;
import org.buldakov.spark.wrapper.parameters.MethodParameter;
import spark.Service;

import java.util.Collections;

@ActionController
public class Test {

    @Action("/test1")
    public String testMethod1(@Body Long a, @GetParam("b") String b) {
        return a + " " + b;
    }

    @Action("/test2")
    public String testMethod2(@Body String a, @GetParam("b") Long b) {
        return a + " " + b;
    }

    @Action("/test3")
    public String testMethod3(@Body Pojo a, @GetParam("b") String b) {
        return a + " " + b;
    }

    @Action("/test4")
    public String testMethod4(@Body Pojo a, @GetParam("b") String b, @HeaderParam("X-Test-Heare") Long test) {
        return a + " " + b + " " + test;
    }

    @Action("/test5/:id")
    public String testMethod5(@PathParam(":id") String id, @Body Pojo a, Long nullable, @GetParam("b") String b,
                              @HeaderParam("X-Test-Header") Long test)
    {
        return a + " " + b + " " + test;
    }

    public static void main(String[] args) {
        Test test = new Test();
        ActionInvocationResolver resolver = ActionInvocationResolver.builder()
                .withDefaultBinders()
                .withDefaultConverters()
                .withConverter(new PojoTypeConverter())
                .build();
        Service service = Service.ignite();

        ServiceConfigurator configurator = new ServiceConfigurator(service, resolver, Collections.singletonList(test), Object::toString);
        configurator.init();
    }

    static class Pojo {

        int a;
        String b;

        public Pojo(int a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    private static class PojoTypeConverter implements TypeConverter<Pojo> {

        @Override
        public Pojo convert(MethodParameter<?> parameter, String value) {
            return new Pojo(Integer.parseInt(value), value);
        }

        @Override
        public boolean canHandle(MethodParameter<?> parameter) {
            return parameter.getType().isAssignableFrom(Pojo.class);
        }

    }

}
