package com.erendogan6.planmyworkout.di;

import com.erendogan6.planmyworkout.domain.repository.AuthRepository;
import com.erendogan6.planmyworkout.domain.usecase.auth.IsUserLoggedInUseCase;
import com.erendogan6.planmyworkout.domain.usecase.auth.LoginUseCase;
import com.erendogan6.planmyworkout.domain.usecase.auth.RegisterUseCase;
import com.erendogan6.planmyworkout.domain.usecase.auth.ResetPasswordUseCase;
import com.erendogan6.planmyworkout.domain.usecase.auth.SignOutUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for providing use case dependencies.
 */
@Module
@InstallIn(SingletonComponent.class)
public class UseCaseModule {
    
    /**
     * Provides LoginUseCase instance.
     *
     * @param authRepository AuthRepository instance
     * @return LoginUseCase instance
     */
    @Provides
    @Singleton
    public LoginUseCase provideLoginUseCase(AuthRepository authRepository) {
        return new LoginUseCase(authRepository);
    }
    
    /**
     * Provides RegisterUseCase instance.
     *
     * @param authRepository AuthRepository instance
     * @return RegisterUseCase instance
     */
    @Provides
    @Singleton
    public RegisterUseCase provideRegisterUseCase(AuthRepository authRepository) {
        return new RegisterUseCase(authRepository);
    }
    
    /**
     * Provides ResetPasswordUseCase instance.
     *
     * @param authRepository AuthRepository instance
     * @return ResetPasswordUseCase instance
     */
    @Provides
    @Singleton
    public ResetPasswordUseCase provideResetPasswordUseCase(AuthRepository authRepository) {
        return new ResetPasswordUseCase(authRepository);
    }
    
    /**
     * Provides IsUserLoggedInUseCase instance.
     *
     * @param authRepository AuthRepository instance
     * @return IsUserLoggedInUseCase instance
     */
    @Provides
    @Singleton
    public IsUserLoggedInUseCase provideIsUserLoggedInUseCase(AuthRepository authRepository) {
        return new IsUserLoggedInUseCase(authRepository);
    }
    
    /**
     * Provides SignOutUseCase instance.
     *
     * @param authRepository AuthRepository instance
     * @return SignOutUseCase instance
     */
    @Provides
    @Singleton
    public SignOutUseCase provideSignOutUseCase(AuthRepository authRepository) {
        return new SignOutUseCase(authRepository);
    }
}
