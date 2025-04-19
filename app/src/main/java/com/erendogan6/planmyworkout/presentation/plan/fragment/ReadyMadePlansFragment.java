package com.erendogan6.planmyworkout.presentation.plan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.FragmentReadyMadePlansBinding;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.presentation.plan.adapter.WorkoutPlanAdapter;
import com.erendogan6.planmyworkout.presentation.plan.viewmodel.ReadyMadePlansViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying ready-made workout plans.
 * This fragment shows a list of predefined workout plans that users can select.
 */
@AndroidEntryPoint
public class ReadyMadePlansFragment extends Fragment implements WorkoutPlanAdapter.OnPlanSelectedListener {

    private FragmentReadyMadePlansBinding binding;
    private WorkoutPlanAdapter adapter;
    private ReadyMadePlansViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReadyMadePlansBinding.inflate(inflater, container, false);
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
        binding.rvWorkoutPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WorkoutPlanAdapter(null, this);
        binding.rvWorkoutPlans.setAdapter(adapter);
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
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                binding.rvWorkoutPlans.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        // Observe empty state
        viewModel.getIsEmpty().observe(getViewLifecycleOwner(), isEmpty -> {
            if (binding != null) {
                binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                binding.rvWorkoutPlans.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void onPlanSelected(WorkoutPlan plan) {
        // Navigate to WorkoutPlanDetailFragment with the selected plan using Navigation Component
        NavController navController = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("workoutPlan", plan);
        navController.navigate(R.id.action_readyMadePlansFragment_to_workoutPlanDetailFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
