package com.erendogan6.planmyworkout.presentation.onboarding;

import android.os.Bundle;

import com.erendogan6.planmyworkout.presentation.common.BaseActivity;
import com.erendogan6.planmyworkout.presentation.onboarding.fragment.OnboardingFragment;
import com.erendogan6.planmyworkout.presentation.plan.fragment.AiPlanGenerationFragment;
import com.erendogan6.planmyworkout.presentation.plan.fragment.CreatePlanFragment;
import com.erendogan6.planmyworkout.presentation.plan.fragment.ReadyMadePlansFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for user onboarding.
 * This activity now uses a fragment-based approach for better modularity.
 */
@AndroidEntryPoint
public class OnboardingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add the OnboardingFragment if this is the first creation
        if (savedInstanceState == null) {
            addFragment(new OnboardingFragment(), false);
        }
    }
}
