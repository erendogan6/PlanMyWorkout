package com.erendogan6.planmyworkout.feature.profile.usecase;

import com.erendogan6.planmyworkout.feature.profile.repository.ProfileRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

/**
 * Use case for logging out the current user.
 */
public class LogoutUseCase {

    private final ProfileRepository profileRepository;

    @Inject
    public LogoutUseCase(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Executes the logout operation.
     *
     * @return Task that completes when logout is successful
     */
    public Task<Void> execute() {
        return profileRepository.logout();
    }
}