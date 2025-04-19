package com.erendogan6.planmyworkout.presentation.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.ActivityReadyMadePlansBinding;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;
import com.erendogan6.planmyworkout.domain.usecase.plan.GetReadyMadePlansUseCase;
import com.erendogan6.planmyworkout.presentation.plan.adapter.WorkoutPlanAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for displaying ready-made workout plans.
 * This activity shows a list of predefined workout plans that users can select.
 */
@AndroidEntryPoint
public class ReadyMadePlansActivity extends AppCompatActivity implements WorkoutPlanAdapter.OnPlanSelectedListener {

    private ActivityReadyMadePlansBinding binding;
    private WorkoutPlanAdapter adapter;

    @Inject
    GetReadyMadePlansUseCase getReadyMadePlansUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityReadyMadePlansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the RecyclerView
        setupRecyclerView();

        // Load workout plans
        loadWorkoutPlans();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.rvWorkoutPlans;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkoutPlanAdapter(null, this);
        recyclerView.setAdapter(adapter);
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
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.rvWorkoutPlans.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean isEmpty) {
        binding.tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvWorkoutPlans.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPlanSelected(WorkoutPlan plan) {
        // Navigate to WorkoutPlanDetailActivity with the selected plan
        Intent intent = new Intent(this, WorkoutPlanDetailActivity.class);
        intent.putExtra(WorkoutPlanDetailActivity.EXTRA_WORKOUT_PLAN, plan);
        startActivity(intent);
    }
}
