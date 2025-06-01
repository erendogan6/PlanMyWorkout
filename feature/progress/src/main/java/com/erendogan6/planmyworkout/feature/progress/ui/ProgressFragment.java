package com.erendogan6.planmyworkout.feature.progress.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;
import com.erendogan6.planmyworkout.feature.progress.adapter.ExerciseDetailAdapter;
import com.erendogan6.planmyworkout.feature.progress.databinding.FragmentProgressBinding;
import com.erendogan6.planmyworkout.feature.progress.viewmodel.ProgressViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for viewing workout progress and history.
 */
@AndroidEntryPoint
public class ProgressFragment extends BaseFragment {

    private FragmentProgressBinding binding;
    private ProgressViewModel viewModel;
    private ExerciseDetailAdapter exerciseDetailAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        setupCalendar();
        setupRecyclerView();
        setupListeners();
        observeViewModel();

        // Load today's workout data initially
        LocalDate today = LocalDate.now();
        viewModel.loadWorkoutDataForDate(today);
        updateSelectedDateDisplay(today);
    }

    private void setupCalendar() {
        // Set up the calendar for the current week
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);

        // Update calendar dates
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            updateCalendarDay(i, date, date.equals(today));
        }
    }

    private void updateCalendarDay(int dayIndex, LocalDate date, boolean isSelected) {
        View dayView = null;
        TextView dayLabel = null;
        TextView dateLabel = null;

        switch (dayIndex) {
            case 0:
                dayView = binding.calendarDay1;
                dayLabel = binding.tvDay1;
                dateLabel = binding.tvDate1;
                break;
            case 1:
                dayView = binding.calendarDay2;
                dayLabel = binding.tvDay2;
                dateLabel = binding.tvDate2;
                break;
            case 2:
                dayView = binding.calendarDay3;
                dayLabel = binding.tvDay3;
                dateLabel = binding.tvDate3;
                break;
            case 3:
                dayView = binding.calendarDay4;
                dayLabel = binding.tvDay4;
                dateLabel = binding.tvDate4;
                break;
            case 4:
                dayView = binding.calendarDay5;
                dayLabel = binding.tvDay5;
                dateLabel = binding.tvDate5;
                break;
            case 5:
                dayView = binding.calendarDay6;
                dayLabel = binding.tvDay6;
                dateLabel = binding.tvDate6;
                break;
            case 6:
                dayView = binding.calendarDay7;
                dayLabel = binding.tvDay7;
                dateLabel = binding.tvDate7;
                break;
        }

        if (dayView != null && dayLabel != null && dateLabel != null) {
            // Set day label (M, T, W, T, F, S, S)
            String[] dayLabels = {"M", "T", "W", "T", "F", "S", "S"};
            dayLabel.setText(dayLabels[dayIndex]);

            // Set date number
            dateLabel.setText(String.valueOf(date.getDayOfMonth()));

            // Set selection state
            dayView.setSelected(isSelected);

            // Set click listener
            dayView.setOnClickListener(v -> selectDate(date));
        }
    }

    private void setupRecyclerView() {
        exerciseDetailAdapter = new ExerciseDetailAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvExerciseDetails.setLayoutManager(layoutManager);
        binding.rvExerciseDetails.setAdapter(exerciseDetailAdapter);
        binding.rvExerciseDetails.setNestedScrollingEnabled(false);
        binding.rvExerciseDetails.setHasFixedSize(false);
    }

    private void setupListeners() {
        binding.btnPreviousWeek.setOnClickListener(v -> {
            viewModel.navigateToPreviousWeek();
        });

        binding.btnNextWeek.setOnClickListener(v -> {
            viewModel.navigateToNextWeek();
        });

        binding.btnSelectDate.setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void observeViewModel() {
        // Observe selected date
        viewModel.getSelectedDate().observe(getViewLifecycleOwner(), date -> {
            if (date != null) {
                updateSelectedDateDisplay(date);
                viewModel.loadWorkoutDataForDate(date);
            }
        });

        // Observe current week
        viewModel.getCurrentWeek().observe(getViewLifecycleOwner(), weekDates -> {
            if (weekDates != null && weekDates.size() == 7) {
                for (int i = 0; i < 7; i++) {
                    LocalDate date = weekDates.get(i);
                    boolean isSelected = date.equals(viewModel.getSelectedDate().getValue());
                    updateCalendarDay(i, date, isSelected);
                }
            }
        });

        // Observe workout summary
        viewModel.getWorkoutSummary().observe(getViewLifecycleOwner(), summary -> {
            if (summary != null) {
                binding.tvTotalSets.setText(String.valueOf(summary.getTotalSets()));
                binding.tvTotalReps.setText(String.valueOf(summary.getTotalReps()));
                binding.tvTotalExercises.setText(String.valueOf(summary.getTotalExercises()));

                binding.layoutSummaryStats.setVisibility(View.VISIBLE);
                binding.layoutNoData.setVisibility(View.GONE);
            } else {
                binding.layoutSummaryStats.setVisibility(View.GONE);
                binding.layoutNoData.setVisibility(View.VISIBLE);
            }
        });

        // Observe exercise details
        viewModel.getExerciseDetails().observe(getViewLifecycleOwner(), exerciseDetails -> {
            if (exerciseDetails != null && !exerciseDetails.isEmpty()) {
                exerciseDetailAdapter.updateExercises(exerciseDetails);
                binding.rvExerciseDetails.setVisibility(View.VISIBLE);
            } else {
                binding.rvExerciseDetails.setVisibility(View.GONE);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectDate(LocalDate date) {
        viewModel.selectDate(date);
    }

    private void updateSelectedDateDisplay(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");
        binding.tvSelectedDate.setText(date.format(formatter));

        // Update calendar selection
        List<LocalDate> weekDates = viewModel.getCurrentWeek().getValue();
        if (weekDates != null) {
            for (int i = 0; i < weekDates.size(); i++) {
                LocalDate weekDate = weekDates.get(i);
                updateCalendarDay(i, weekDate, weekDate.equals(date));
            }
        }
    }

    private void showDatePicker() {
        LocalDate currentDate = viewModel.getSelectedDate().getValue();
        if (currentDate == null) currentDate = LocalDate.now();

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    viewModel.selectDate(selectedDate);
                },
                currentDate.getYear(),
                currentDate.getMonthValue() - 1,
                currentDate.getDayOfMonth()
        );

        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}