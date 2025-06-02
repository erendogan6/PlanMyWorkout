package com.erendogan6.planmyworkout.feature.workout.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;
import com.erendogan6.planmyworkout.feature.workout.adapter.ExerciseLogAdapter;
import com.erendogan6.planmyworkout.feature.workout.databinding.FragmentExerciseHistoryBinding;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLogHeader;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLogItem;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLogItemWrapper;
import com.erendogan6.planmyworkout.feature.workout.util.SwipeToActionHelper;
import com.erendogan6.planmyworkout.feature.workout.viewmodel.ExerciseHistoryViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;
import coil.target.Target;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying the history of logs for a specific exercise.
 */
@AndroidEntryPoint
public class ExerciseHistoryFragment extends BaseFragment implements
        ExerciseLogAdapter.OnLogSelectedListener,
        ExerciseLogAdapter.OnLogActionListener {

    FragmentExerciseHistoryBinding binding;
    ExerciseHistoryViewModel viewModel;
    private ExerciseLogAdapter adapter;
    private String currentExerciseName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExerciseHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ExerciseHistoryViewModel.class);

        // Set up back button
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        // Set up video button
        binding.btnWatchVideo.setOnClickListener(v -> openYouTubeSearch());

        // Set up RecyclerView
        setupRecyclerView();

        // Set up FAB
        binding.fabAddLog.setOnClickListener(v -> navigateToExerciseDetail(false, null));

        // Load exercise data
        viewModel.loadExerciseData();

        // Observe ViewModel
        observeViewModel();
    }

    private void setupRecyclerView() {
        binding.rvLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExerciseLogAdapter(new ArrayList<>(), this);
        adapter.setOnLogActionListener(this);
        binding.rvLogs.setAdapter(adapter);

        // Update swipe helper to handle different view types
        SwipeToActionHelper swipeHelper = new SwipeToActionHelper(requireContext(), adapter) {
            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof ExerciseLogAdapter.HeaderViewHolder) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
        itemTouchHelper.attachToRecyclerView(binding.rvLogs);
    }

    @Override
    public void onDeleteLog(ExerciseLog log, int position) {
        // Get the item from adapter before removing
        ExerciseLogItem removedItem = adapter.getItem(position);
        adapter.removeItem(position);

        Snackbar.make(binding.getRoot(), "Log deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> adapter.restoreItem(removedItem, position)) // ExerciseLogItem kullan
                .setActionTextColor(getResources().getColor(com.erendogan6.planmyworkout.coreui.R.color.accent))
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event != DISMISS_EVENT_ACTION) {
                            viewModel.deleteLog(log);
                        }
                    }
                })
                .show();
    }

    @Override
    public void onDuplicateLog(ExerciseLog log, int position) {
        viewModel.duplicateLog(log);
    }

    private void observeViewModel() {
        // Observe exercise
        viewModel.getExercise().observe(getViewLifecycleOwner(), exercise -> {
            if (exercise != null) {
                currentExerciseName = exercise.getName();
                binding.tvExerciseName.setText(currentExerciseName);

                // Set muscle group if available
                if (exercise.getMuscleGroup() != null && !exercise.getMuscleGroup().isEmpty()) {
                    binding.tvMuscleGroup.setText(exercise.getMuscleGroup());
                    binding.tvMuscleGroup.setVisibility(View.VISIBLE);
                } else {
                    binding.tvMuscleGroup.setVisibility(View.GONE);
                }
            }
        });

        // Observe action message
        viewModel.getActionMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                viewModel.clearActionMessage();
            }
        });

        // Observe exercise image
        viewModel.getExerciseImageUrl().observe(getViewLifecycleOwner(), imageUrl -> {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                ImageRequest request = new ImageRequest.Builder(requireContext())
                        .data(imageUrl)
                        .target(new Target() {
                            @Override
                            public void onStart(@Nullable Drawable placeholder) {
                                binding.ivExerciseImage.setImageDrawable(placeholder);
                            }

                            @Override
                            public void onSuccess(@NonNull Drawable result) {
                                binding.ivExerciseImage.setImageDrawable(result);
                                binding.imageLoadingOverlay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(@Nullable Drawable error) {
                                binding.ivExerciseImage.setImageDrawable(error);
                                binding.imageLoadingOverlay.setVisibility(View.GONE);
                            }
                        })
                        .placeholder(com.erendogan6.planmyworkout.coreui.R.drawable.ic_placeholder_exercise)
                        .error(com.erendogan6.planmyworkout.coreui.R.drawable.ic_placeholder_exercise)
                        .crossfade(true)
                        .build();

                Coil.imageLoader(requireContext()).enqueue(request);
            } else {
                binding.imageLoadingOverlay.setVisibility(View.GONE);
                binding.ivExerciseImage.setImageResource(com.erendogan6.planmyworkout.coreui.R.drawable.ic_placeholder_exercise);
            }
        });

        // Observe logs
        viewModel.getLogs().observe(getViewLifecycleOwner(), logs -> {
            if (logs != null) {
                List<ExerciseLogItem> groupedItems = groupLogsByDate(logs);
                adapter.updateLogs(groupedItems);
                updateEmptyState(logs.isEmpty());
            } else {
                updateEmptyState(true);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
                binding.rvLogs.setVisibility(View.GONE);
            } else {
                hideLoading();
                binding.rvLogs.setVisibility(View.VISIBLE);
            }
        });

        // Observe error message
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<ExerciseLogItem> groupLogsByDate(List<ExerciseLog> logs) {
        List<ExerciseLogItem> groupedItems = new ArrayList<>();

        if (logs.isEmpty()) {
            return groupedItems;
        }

        // Sort logs by date descending
        List<ExerciseLog> sortedLogs = new ArrayList<>(logs);
        sortedLogs.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        String currentDate = null;

        for (ExerciseLog log : sortedLogs) {
            String logDate = log.getFormattedDateHeader();

            // Add header if date changed
            if (!logDate.equals(currentDate)) {
                groupedItems.add(new ExerciseLogHeader(logDate));
                currentDate = logDate;
            }

            // Add log item
            groupedItems.add(new ExerciseLogItemWrapper(log));
        }

        return groupedItems;
    }

    /**
     * Opens YouTube search for the current exercise
     */
    private void openYouTubeSearch() {
        if (currentExerciseName == null || currentExerciseName.isEmpty()) {
            Toast.makeText(requireContext(), "Exercise name not available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Format search query: "how to" + exercise name
            String searchQuery = "how to " + currentExerciseName.toLowerCase();

            // Replace spaces with + for URL
            String encodedQuery = searchQuery.replace(" ", "+");

            // Create YouTube search URL
            String youtubeSearchUrl = "https://www.youtube.com/results?search_query=" + encodedQuery;

            // Try to open in YouTube app first
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeSearchUrl));
            youtubeIntent.setPackage("com.google.android.youtube");

            if (youtubeIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                // YouTube app is available, open in YouTube app
                startActivity(youtubeIntent);
            } else {
                // YouTube app not available, open in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeSearchUrl));

                if (browserIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(browserIntent);
                } else {
                    // Fallback: Create chooser to let user pick an app
                    Intent chooserIntent = Intent.createChooser(browserIntent, "Watch exercise video");
                    startActivity(chooserIntent);
                }
            }

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error opening YouTube search", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyState(boolean isEmpty) {
        binding.layoutEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvLogs.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onLogSelected(ExerciseLog log) {
        // Navigate to exercise detail for editing
        navigateToExerciseDetail(true, log);
    }

    private void navigateToExerciseDetail(boolean isEdit, ExerciseLog log) {
        String exerciseId = viewModel.getExerciseId();
        String planId = viewModel.getPlanId();

        if (exerciseId == null || planId == null) {
            Toast.makeText(requireContext(), "Exercise ID or Plan ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        ExerciseHistoryFragmentDirections.ActionExerciseHistoryFragmentToExerciseDetailFragment action =
                ExerciseHistoryFragmentDirections.actionExerciseHistoryFragmentToExerciseDetailFragment(
                        exerciseId, planId);

        // Set edit mode and log ID if editing
        if (isEdit && log != null) {
            action.setIsEdit(true);
            action.setLogId(log.getId());
            action.setWeight((float) log.getWeight());
            action.setReps(log.getReps());
            action.setNotes(log.getNotes() != null ? log.getNotes() : "");
        }

        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh logs when returning to this fragment
        viewModel.refreshLogs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}