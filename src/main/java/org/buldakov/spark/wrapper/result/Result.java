package org.buldakov.spark.wrapper.result;

public class Result<T> {

    private final int code;
    private final T value;

    private Result(int code, T value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public T getValue() {
        return value;
    }

    public static <V> Result<V> status(int code, V value) {
        return new Result<>(code, value);
    }

    //https://httpstatuses.com/200
    public static <V> Result<V> ok(V value) {
        return new Result<>(200, value);
    }

    //https://httpstatuses.com/422
    public static Result<String> unprocessableEntity(String message) {
        return new Result<>(422, message);
    }

    //https://httpstatuses.com/404
    public static Result<String> notFound(String message) {
        return new Result<>(404, message);
    }

    //https://httpstatuses.com/401
    public static Result<String> unauthorized(String message) {
        return new Result<>(401, message);
    }

    //https://httpstatuses.com/500
    public static Result<String> internalServerError(String message) {
        return new Result<>(500, message);
    }
}
