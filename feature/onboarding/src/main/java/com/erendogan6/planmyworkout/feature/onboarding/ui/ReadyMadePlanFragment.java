package com.erendogan6.planmyworkout.feature.onboarding.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;

import com.erendogan6.planmyworkout.feature.onboarding.adapter.WorkoutPlanAdapter;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentReadyMadePlanListBinding;
import com.erendogan6.planmyworkout.feature.onboarding.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.onboarding.viewmodel.ReadyMadePlansViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying ready-made workout plans.
 * This fragment shows a list of predefined workout plans that users can select.
 */
@AndroidEntryPoint
public class ReadyMadePlanFragment extends BaseFragment implements WorkoutPlanAdapter.OnPlanSelectedListener {

    private FragmentReadyMadePlanListBinding binding;
    private WorkoutPlanAdapter adapter;
    private ReadyMadePlansViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReadyMadePlanListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ReadyMadePlansViewModel.class);

        // Set up the RecyclerView
        setupRecyclerView();

        // Observe ViewModel
        observeViewModel();

        // Load workout plans
        viewModel.loadWorkoutPlans();
    }

    private void setupRecyclerView() {
        binding.recyclerViewPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WorkoutPlanAdapter(null, this);
        binding.recyclerViewPlans.setAdapter(adapter);
    }

    private void observeViewModel() {
        // Observe workout plans
        viewModel.getWorkoutPlans().observe(getViewLifecycleOwner(), plans -> {
            if (plans != null) {
                adapter.updatePlans(plans);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (binding != null) {
                if (Boolean.TRUE.equals(isLoading)) {
                    showLoading();
                    binding.recyclerViewPlans.setVisibility(View.GONE);
                } else {
                    updateUIAfterLoading();
                }
            }
        });

        // Observe empty state
        viewModel.getIsEmpty().observe(getViewLifecycleOwner(), isEmpty -> {
            if (binding != null) {
                binding.textViewEmpty.setVisibility(Boolean.TRUE.equals(isEmpty) ? View.VISIBLE : View.GONE);
                binding.recyclerViewPlans.setVisibility(Boolean.TRUE.equals(isEmpty) ? View.GONE : View.VISIBLE);
            }
        });

        // Observe error message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (binding != null && errorMessage != null && !errorMessage.isEmpty()) {
                // Show a toast with the error message
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUIAfterLoading() {
        binding.recyclerViewPlans.setVisibility(View.VISIBLE);

        binding.recyclerViewPlans.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                binding.recyclerViewPlans.getViewTreeObserver().removeOnPreDrawListener(this);

                if (adapter != null && adapter.getItemCount() > 0) {
                    hideLoading();
                } else if (viewModel.getIsEmpty().getValue() == Boolean.TRUE) {
                    hideLoading();
                }
                return true;
            }
        });
    }

    @Override
    public void onPlanSelected(WorkoutPlan plan) {
        // Navigate to PlanDetailFragment with the selected plan ID
        ReadyMadePlanFragmentDirections.ActionReadyMadePlanFragmentToPlanDetailFragment action =
                ReadyMadePlanFragmentDirections.actionReadyMadePlanFragmentToPlanDetailFragment(plan.getId());
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
