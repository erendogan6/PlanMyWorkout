package com.erendogan6.planmyworkout.domain.usecase.base;

import androidx.lifecycle.LiveData;

/**
 * Base UseCase interface for operations that return LiveData.
 *
 * @param <P> Type of the input parameters
 * @param <R> Type of the result
 */
public interface LiveDataUseCase<P, R> extends UseCase {
    
    /**
     * Execute the use case with the given parameters.
     *
     * @param params Input parameters
     * @return LiveData emitting the result
     */
    LiveData<R> execute(P params);
}
