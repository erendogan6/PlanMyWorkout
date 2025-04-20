package com.erendogan6.planmyworkout.domain.model;

/**
 * A generic class that holds a result with a status.
 * @param <T> Type of the result data
 */
public class Result<T> {
    
    private final Status status;
    private final T data;
    private final String message;
    
    private Result(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public T getData() {
        return data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
    
    public boolean isLoading() {
        return status == Status.LOADING;
    }
    
    public boolean isError() {
        return status == Status.ERROR;
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(Status.SUCCESS, data, null);
    }
    
    public static <T> Result<T> error(String message, T data) {
        return new Result<>(Status.ERROR, data, message);
    }
    
    public static <T> Result<T> loading(T data) {
        return new Result<>(Status.LOADING, data, null);
    }
    
    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
