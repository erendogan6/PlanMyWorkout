package com.erendogan6.planmyworkout.feature.auth.model;

/**
 * A sealed interface that represents the result of an authentication operation.
 * It can either be a Success containing data or an Error containing error information.
 *
 * @param <T> The type of data in case of success
 */
public sealed interface AuthResponse<T> permits AuthResponse.Success, AuthResponse.Error {

    /**
     * Checks if the response represents a successful operation.
     *
     * @return true if the operation was successful, false otherwise
     */
    boolean isSuccess();

    /**
     * Gets the data from a successful response.
     *
     * @return The data if the response is successful, null otherwise
     */
    T getData();

    /**
     * Gets the error message from a failed response.
     *
     * @return The error message if the response is an error, null otherwise
     */
    String getErrorMessage();

    /**
     * Gets the cause of the error from a failed response.
     *
     * @return The cause if the response is an error, null otherwise
     */
    Throwable getCause();

    /**
     * Represents a successful authentication operation.
     *
     * @param <T> The type of data
     */
    record Success<T>(T data) implements AuthResponse<T> {
        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public T getData() {
            return data;
        }

        @Override
        public String getErrorMessage() {
            return null;
        }

        @Override
        public Throwable getCause() {
            return null;
        }
    }

    /**
     * Represents a failed authentication operation.
     *
     * @param <T> The type of data that would have been returned if successful
     */
    record Error<T>(String message, Throwable cause) implements AuthResponse<T> {
        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public T getData() {
            return null;
        }

        @Override
        public String getErrorMessage() {
            return message;
        }

        @Override
        public Throwable getCause() {
            return cause;
        }
    }
}