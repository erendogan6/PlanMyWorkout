package com.erendogan6.planmyworkout.feature.onboarding.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentOnboardingSelectionBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for selecting the onboarding path.
 */
@AndroidEntryPoint
public class OnboardingSelectionFragment extends BaseFragment {

    private FragmentOnboardingSelectionBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOnboardingSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();
    }

    private void setupListeners() {
        binding.cardReadyMade.setOnClickListener(v -> handleChoiceSelection(R.id.action_onboardingSelectionFragment_to_readyMadePlanFragment));

        binding.cardCreateOwn.setOnClickListener(v -> handleChoiceSelection(R.id.action_onboardingSelectionFragment_to_createOwnPlanFragment));

        binding.cardAiGenerate.setOnClickListener(v -> handleChoiceSelection(R.id.action_onboardingSelectionFragment_to_aiGeneratePlanFragment));
    }

    private void handleChoiceSelection(int navigationAction) {
        Navigation.findNavController(requireView()).navigate(navigationAction);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
