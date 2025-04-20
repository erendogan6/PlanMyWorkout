package com.erendogan6.planmyworkout.feature.onboarding.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentOnboardingSelectionBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.OnboardingChoice;
import com.erendogan6.planmyworkout.feature.onboarding.viewmodel.OnboardingViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for selecting the onboarding path.
 */
@AndroidEntryPoint
public class OnboardingSelectionFragment extends Fragment {

    private FragmentOnboardingSelectionBinding binding;
    private OnboardingViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOnboardingSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getSaveResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isLoading()) {
                // Show loading state
                setCardsEnabled(false);
            } else {
                setCardsEnabled(true);

                if (result.isError()) {
                    // Show error message
                    Toast.makeText(requireContext(), "Error: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setCardsEnabled(boolean enabled) {
        binding.cardReadyMade.setEnabled(enabled);
        binding.cardCreateOwn.setEnabled(enabled);
        binding.cardAiGenerate.setEnabled(enabled);
    }

    private void setupListeners() {
        binding.cardReadyMade.setOnClickListener(v -> {
            handleChoiceSelection(OnboardingChoice.READY_MADE, R.id.action_onboardingSelectionFragment_to_readyMadePlanFragment);
        });

        binding.cardCreateOwn.setOnClickListener(v -> {
            handleChoiceSelection(OnboardingChoice.CREATE_OWN, R.id.action_onboardingSelectionFragment_to_createOwnPlanFragment);
        });

        binding.cardAiGenerate.setOnClickListener(v -> {
            handleChoiceSelection(OnboardingChoice.AI_GENERATE, R.id.action_onboardingSelectionFragment_to_aiGeneratePlanFragment);
        });
    }

    private void handleChoiceSelection(OnboardingChoice choice, int navigationAction) {
        viewModel.saveOnboardingChoice(choice).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                // Navigate to the next screen
                Navigation.findNavController(requireView()).navigate(navigationAction);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
