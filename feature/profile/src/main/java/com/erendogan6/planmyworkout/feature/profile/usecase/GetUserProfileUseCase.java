package com.erendogan6.planmyworkout.feature.profile.usecase;

import com.erendogan6.planmyworkout.feature.profile.model.UserProfile;
import com.erendogan6.planmyworkout.feature.profile.repository.ProfileRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

/**
 * Use case for getting user profile information.
 */
public class GetUserProfileUseCase {

    private final ProfileRepository profileRepository;

    @Inject
    public GetUserProfileUseCase(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Executes the get user profile operation.
     *
     * @return Task containing the user profile
     */
    public Task<UserProfile> execute() {
        return profileRepository.getUserProfile();
    }
}