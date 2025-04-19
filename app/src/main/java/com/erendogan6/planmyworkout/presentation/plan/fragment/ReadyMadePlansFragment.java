package com.erendogan6.planmyworkout.presentation.plan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.databinding.FragmentReadyMadePlansBinding;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.domain.usecase.plan.GetReadyMadePlansUseCase;
import com.erendogan6.planmyworkout.presentation.common.BaseActivity;
import com.erendogan6.planmyworkout.presentation.plan.adapter.WorkoutPlanAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying ready-made workout plans.
 * This fragment shows a list of predefined workout plans that users can select.
 */
@AndroidEntryPoint
public class ReadyMadePlansFragment extends Fragment implements WorkoutPlanAdapter.OnPlanSelectedListener {

    private FragmentReadyMadePlansBinding binding;
    private WorkoutPlanAdapter adapter;
    
    @Inject
    GetReadyMadePlansUseCase getReadyMadePlansUseCase;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReadyMadePlansBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Set up the RecyclerView
        setupRecyclerView();
        
        // Load workout plans
        loadWorkoutPlans();
    }
    
    private void setupRecyclerView() {
        binding.rvWorkoutPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WorkoutPlanAdapter(null, this);
        binding.rvWorkoutPlans.setAdapter(adapter);
    }
    
    private void loadWorkoutPlans() {
        // Show loading state
        showLoading(true);
        
        // Get workout plans from use case
        List<WorkoutPlan> plans = getReadyMadePlansUseCase.execute();
        
        // Update UI based on result
        if (plans != null && !plans.isEmpty()) {
            adapter.updatePlans(plans);
            showEmptyState(false);
        } else {
            showEmptyState(true);
        }
        
        // Hide loading state
        showLoading(false);
    }
    
    private void showLoading(boolean isLoading) {
        if (binding != null) {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.rvWorkoutPlans.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }
    
    private void showEmptyState(boolean isEmpty) {
        if (binding != null) {
            binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.rvWorkoutPlans.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
    }
    
    @Override
    public void onPlanSelected(WorkoutPlan plan) {
        // Navigate to WorkoutPlanDetailFragment with the selected plan
        WorkoutPlanDetailFragment detailFragment = new WorkoutPlanDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(WorkoutPlanDetailFragment.ARG_WORKOUT_PLAN, plan);
        detailFragment.setArguments(args);
        
        // Use the BaseActivity to handle fragment transactions
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).replaceFragment(detailFragment, true);
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
