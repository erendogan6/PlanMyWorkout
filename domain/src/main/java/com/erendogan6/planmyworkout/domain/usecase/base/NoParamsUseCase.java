package com.erendogan6.planmyworkout.domain.usecase.base;

/**
 * Base UseCase interface for operations that don't require parameters.
 *
 * @param <R> Type of the result
 */
public interface NoParamsUseCase<R> extends UseCase {
    
    /**
     * Execute the use case without parameters.
     *
     * @return Result of the operation
     */
    R execute();
}
